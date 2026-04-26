package maia.dmt.core.data.sensors.analysis

import android.util.Log
import maia.dmt.core.data.sensors.util.SensorMathUtils
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.SensorsData
import org.jtransforms.fft.FloatFFT_1D
import kotlin.math.pow
import kotlin.math.sqrt

class TremorAnalysisUseCase {

    companion object {
        private const val TAG = "TremorDebug"
        private const val SAMPLE_RATE = 50f

        // Parkinson's tremor band: 3.5–7.0 Hz (wider than previous 4–6 to catch patient variation)
        private const val TREMOR_FREQ_LOW = 3.5f
        private const val TREMOR_FREQ_HIGH = 7.0f

        // Power ratio threshold — if >18% of motion energy is in tremor band, flag it
        private const val POWER_RATIO_THRESHOLD = 0.18f

        // Minimum total power to avoid triggering on noise when phone is nearly still
        private const val MIN_TOTAL_POWER = 0.05f

        // Duration gate: tremor must be sustained for 10 seconds
        private const val MIN_TREMOR_DURATION = 10_000L

        // Allow up to 2 consecutive missed cycles before resetting the duration gate
        private const val MAX_MISSED_CYCLES = 2

        // Stability: very low acceleration magnitude = phone on table
        private const val STABLE_ACCEL_THRESHOLD = 0.1f
        private const val STABLE_GYRO_THRESHOLD = 0.5f

        // Light threshold lowered — only filter truly dark (phone sealed in pocket)
        private const val POCKET_LIGHT_THRESHOLD = 3.0f

        // Walking rejection: dual-signal (step-rate + gait fundamental frequency)
        private const val MIN_WALKING_STEPS = 5L
        private const val GAIT_FREQ_LOW = 1.0f
        private const val GAIT_FREQ_HIGH = 3.0f
        private const val GAIT_POWER_RATIO_THRESHOLD = 0.15f
    }

    private var tremorStartTime: Long = 0L
    private var missedCycles: Int = 0

    /**
     * Checks if the phone is on a table (both accel AND gyro very low).
     * Light alone is no longer enough to skip analysis — dark rooms are valid.
     */
    fun isPhoneStableOrInPocket(
        accelX: Float, accelY: Float, accelZ: Float,
        gyroX: Float, gyroY: Float, gyroZ: Float,
        lightLux: Float
    ): Boolean {
        val accelMag = SensorMathUtils.calculateMagnitude(accelX, accelY, accelZ)
        val gyroMag = SensorMathUtils.calculateMagnitude(gyroX, gyroY, gyroZ)

        // Phone on table: near-zero movement AND near-zero rotation
        if (accelMag < STABLE_ACCEL_THRESHOLD && gyroMag < STABLE_GYRO_THRESHOLD) {
            return true
        }

        // In pocket: very dark AND no significant movement (both required)
        if (lightLux < POCKET_LIGHT_THRESHOLD && accelMag < STABLE_ACCEL_THRESHOLD) {
            return true
        }

        return false
    }

    /**
     * Main analysis using spectral power ratio approach.
     * @param accelBuffer List of averaged items (for stats / stability check)
     * @param rawAccelList List of 256+ raw items (for FFT frequency analysis)
     */
    fun analyze(
        accelBuffer: List<Acceleration>,
        gyroBuffer: List<Gyroscope>,
        stepBuffer: List<Long>,
        lightBuffer: List<Float>,
        deletionBuffer: List<Int>,
        rawAccelList: List<Acceleration>
    ): SensorsData? {

        if (accelBuffer.size < 30 || rawAccelList.size < 128) {
            Log.d(TAG, "Skipping: accelBuffer=${accelBuffer.size}, rawAccel=${rawAccelList.size}")
            return null
        }

        Log.d(TAG, "analyze() called: accelBuf=${accelBuffer.size}, rawAccel=${rawAccelList.size}, gyro=${gyroBuffer.size}")

        // Stability check using average of magnitudes from raw data (not magnitude of averages)
        // This prevents tremor oscillations from canceling out to near-zero
        val avgAccelMag = rawAccelList.map { SensorMathUtils.calculateMagnitude(it.x, it.y, it.z) }.average().toFloat()
        val avgGyroMag = if (gyroBuffer.isNotEmpty()) {
            gyroBuffer.map { SensorMathUtils.calculateMagnitude(it.x, it.y, it.z) }.average().toFloat()
        } else 0f
        val avgLight = if (lightBuffer.isNotEmpty()) lightBuffer.average().toFloat() else 50f

        Log.d(TAG, "Stability: avgAccelMag=$avgAccelMag, avgGyroMag=$avgGyroMag, light=$avgLight")

        // Use raw magnitudes for stability check
        if (avgAccelMag < STABLE_ACCEL_THRESHOLD && avgGyroMag < STABLE_GYRO_THRESHOLD) {
            Log.d(TAG, "REJECTED: phone on table (low movement)")
            tremorStartTime = 0L
            return null
        }
        if (avgLight < POCKET_LIGHT_THRESHOLD && avgAccelMag < STABLE_ACCEL_THRESHOLD) {
            Log.d(TAG, "REJECTED: phone in pocket (dark + low movement)")
            tremorStartTime = 0L
            return null
        }

        // --- Spectral Power Ratio Analysis ---
        // Use acceleration magnitude (orientation-independent)
        val magnitudes = rawAccelList.map { SensorMathUtils.calculateMagnitude(it.x, it.y, it.z) }

        // Take last power-of-2 samples for clean FFT
        val fftSize = Integer.highestOneBit(magnitudes.size) // e.g., 256 from 256+
        val samples = magnitudes.takeLast(fftSize)

        Log.d(TAG, "FFT: inputSize=${rawAccelList.size}, fftSize=$fftSize")

        // Apply Hanning window to reduce spectral leakage
        val windowed = SensorMathUtils.applyHanningWindow(samples.toFloatArray())

        // Apply FFT
        val fftData = applyFFT(windowed)

        // Compute power spectral density and find dominant frequency
        val analysisResult = computeSpectralPowerRatio(fftData, fftSize)

        Log.d(TAG, "FFT result: powerRatio=${analysisResult.powerRatio}, dominantFreq=${analysisResult.dominantFreq}Hz, totalPower=${analysisResult.totalPower}, gaitRatio=${analysisResult.gaitPowerRatio}")

        // --- Walking rejection (dual-signal: step-rate + gait fundamental) ---
        val totalSteps = stepBuffer.sumOf { it }
        val isWalking = totalSteps > MIN_WALKING_STEPS && analysisResult.gaitPowerRatio > GAIT_POWER_RATIO_THRESHOLD
        Log.d(TAG, "Walking check: steps=$totalSteps (threshold=$MIN_WALKING_STEPS), gaitRatio=${analysisResult.gaitPowerRatio} (threshold=$GAIT_POWER_RATIO_THRESHOLD), isWalking=$isWalking")

        if (isWalking) {
            Log.d(TAG, "REJECTED: walking detected (steps=$totalSteps, gaitRatio=${analysisResult.gaitPowerRatio})")
            tremorStartTime = 0L
            return null
        }

        val ratioOk = analysisResult.powerRatio > POWER_RATIO_THRESHOLD
        val powerOk = analysisResult.totalPower > MIN_TOTAL_POWER
        val freqOk = analysisResult.dominantFreq in TREMOR_FREQ_LOW..TREMOR_FREQ_HIGH
        val isDetected = ratioOk && powerOk && freqOk

        Log.d(TAG, "Detection: ratioOk=$ratioOk (${analysisResult.powerRatio}>$POWER_RATIO_THRESHOLD), powerOk=$powerOk, freqOk=$freqOk (${analysisResult.dominantFreq} in $TREMOR_FREQ_LOW..$TREMOR_FREQ_HIGH) → detected=$isDetected")

        // Duration gate: must sustain detection for MIN_TREMOR_DURATION
        val gateResult = checkDurationGate(isDetected)
        Log.d(TAG, "Duration gate: isDetected=$isDetected, gatePassed=$gateResult, tremorStart=$tremorStartTime, missed=$missedCycles")

        if (!gateResult) {
            return null
        }

        // Compute stats for the upload payload (keeping existing data model)
        val stdDevX = SensorMathUtils.calculateStandardDeviation(accelBuffer.map { it.x })
        val stdDevZ = SensorMathUtils.calculateStandardDeviation(accelBuffer.map { it.z })
        val stdDevSteps = SensorMathUtils.calculateStandardDeviation(stepBuffer.map { it.toFloat() })

        val deletionsFloats = deletionBuffer.map { it.toFloat() }
        val stdDevDeletions = SensorMathUtils.calculateStandardDeviation(deletionsFloats)

        val rangeX = SensorMathUtils.calculateRange(accelBuffer.map { it.x })
        val rangeZ = SensorMathUtils.calculateRange(accelBuffer.map { it.z })
        val rangeGyroX = SensorMathUtils.calculateRange(gyroBuffer.map { it.x })
        val rangeGyroZ = SensorMathUtils.calculateRange(gyroBuffer.map { it.z })

        return SensorsData(
            avgFrequency = analysisResult.dominantFreq,
            stdDevX = stdDevX,
            stdDevZ = stdDevZ,
            threshold = POWER_RATIO_THRESHOLD,
            rangeX = rangeX,
            rangeZ = rangeZ,
            rangeGyroX = rangeGyroX,
            rangeGyroZ = rangeGyroZ,
            steps = stepBuffer.map { it.toFloat() },
            stdDevSteps = stdDevSteps,
            stdDevDeletions = stdDevDeletions,
            rangeDeletions = deletionsFloats
        )
    }

    /**
     * Returns false on first detection, only true after sustained detection for MIN_TREMOR_DURATION.
     * Allows up to MAX_MISSED_CYCLES consecutive non-detections before resetting.
     */
    private fun checkDurationGate(isDetected: Boolean): Boolean {
        if (!isDetected) {
            if (tremorStartTime != 0L) {
                missedCycles++
                if (missedCycles > MAX_MISSED_CYCLES) {
                    tremorStartTime = 0L
                    missedCycles = 0
                }
            }
            return false
        }

        // Detection is positive — reset missed counter
        missedCycles = 0

        val now = System.currentTimeMillis()
        if (tremorStartTime == 0L) {
            tremorStartTime = now
            return false // First detection — start timing, don't fire yet
        }

        return (now - tremorStartTime) >= MIN_TREMOR_DURATION
    }

    private fun applyFFT(data: FloatArray): FloatArray {
        val fft = FloatFFT_1D(data.size.toLong())
        val fftData = FloatArray(data.size * 2)
        for (i in data.indices) {
            fftData[i] = data[i]
        }
        fft.realForward(fftData)
        return fftData
    }

    private data class SpectralResult(
        val powerRatio: Float,
        val dominantFreq: Float,
        val totalPower: Float,
        val gaitPowerRatio: Float
    )

    /**
     * Computes the ratio of power in the tremor band (3.5–7.0 Hz) to total power (0.5–25 Hz).
     * Also finds the dominant frequency within the tremor band.
     */
    private fun computeSpectralPowerRatio(fftData: FloatArray, fftSize: Int): SpectralResult {
        val n = fftData.size / 2
        val freqResolution = SAMPLE_RATE / fftSize

        var tremorBandPower = 0f
        var gaitBandPower = 0f
        var totalPower = 0f
        var peakAmplitude = 0f
        var dominantFreq = 0f

        for (i in 1 until n) {
            val frequency = i * freqResolution
            if (frequency > SAMPLE_RATE / 2) break // Nyquist

            val real = fftData[i * 2]
            val imag = fftData[i * 2 + 1]
            val power = real.pow(2) + imag.pow(2)

            // Accumulate total power (0.5–25 Hz, skip DC and very low freq)
            if (frequency in 0.5f..25f) {
                totalPower += power
            }

            // Accumulate gait fundamental band power (1.0–3.0 Hz)
            if (frequency in GAIT_FREQ_LOW..GAIT_FREQ_HIGH) {
                gaitBandPower += power
            }

            // Accumulate tremor band power
            if (frequency in TREMOR_FREQ_LOW..TREMOR_FREQ_HIGH) {
                tremorBandPower += power
                val amplitude = sqrt(power)
                if (amplitude > peakAmplitude) {
                    peakAmplitude = amplitude
                    dominantFreq = frequency
                }
            }
        }

        val ratio = if (totalPower > 0f) tremorBandPower / totalPower else 0f
        val gaitRatio = if (totalPower > 0f) gaitBandPower / totalPower else 0f
        return SpectralResult(ratio, dominantFreq, totalPower, gaitRatio)
    }
}
