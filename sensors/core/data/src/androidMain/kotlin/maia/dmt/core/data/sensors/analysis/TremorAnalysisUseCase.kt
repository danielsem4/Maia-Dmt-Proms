package maia.dmt.core.data.sensors.analysis

import android.util.Log
import maia.dmt.core.data.sensors.util.SensorMathUtils
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.SensorsData
import org.jtransforms.fft.FloatFFT_1D
import kotlin.math.pow

class TremorAnalysisUseCase {

    companion object {
        private const val TAG = "TremorDebug"
        private const val DEFAULT_SAMPLE_RATE = 50f

        // Parkinson's tremor band: 3.5–7.0 Hz (wider than previous 4–6 to catch patient variation)
        private const val TREMOR_FREQ_LOW = 3.5f
        private const val TREMOR_FREQ_HIGH = 7.0f

        // Power ratio threshold — if >15% of motion energy is in tremor band, flag it
        private const val POWER_RATIO_THRESHOLD = 0.15f

        // Minimum total power to avoid triggering on noise when phone is nearly still
        // Lowered because gravity-removed data has much less power than raw accelerometer
        private const val MIN_TOTAL_POWER = 0.005f

        // Duration gate: tremor must be sustained for 3 seconds
        private const val MIN_TREMOR_DURATION = 3_000L

        // Allow up to 2 consecutive missed cycles before resetting the duration gate
        private const val MAX_MISSED_CYCLES = 2

        // Stability: very low acceleration magnitude = phone on table
        // Lowered to avoid rejecting mild tremor after gravity removal
        private const val STABLE_ACCEL_THRESHOLD = 0.05f
        private const val STABLE_GYRO_THRESHOLD = 0.5f

        // Light threshold lowered — only filter truly dark (phone sealed in pocket)
        private const val POCKET_LIGHT_THRESHOLD = 3.0f

        // Minimum samples needed to compute a reliable sample rate
        private const val MIN_SAMPLES_FOR_RATE = 10
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

        // --- Spectral Power Ratio Analysis (per-axis to preserve true frequency) ---
        // Using per-axis FFT instead of magnitude FFT to avoid frequency doubling.
        // Magnitude of gravity-removed data acts as full-wave rectification, shifting
        // a 5 Hz tremor to 10 Hz in the spectrum. Per-axis FFT preserves the real frequency.
        val fftSize = Integer.highestOneBit(rawAccelList.size) // e.g., 256 from 256+
        val lastSamples = rawAccelList.takeLast(fftSize)
        val xSamples = lastSamples.map { it.x }.toFloatArray()
        val ySamples = lastSamples.map { it.y }.toFloatArray()
        val zSamples = lastSamples.map { it.z }.toFloatArray()

        val actualSampleRate = computeSampleRate(lastSamples)

        Log.d(TAG, "FFT: inputSize=${rawAccelList.size}, fftSize=$fftSize, sampleRate=$actualSampleRate")

        // Window each axis to reduce spectral leakage
        val xWindowed = SensorMathUtils.applyHanningWindow(xSamples)
        val yWindowed = SensorMathUtils.applyHanningWindow(ySamples)
        val zWindowed = SensorMathUtils.applyHanningWindow(zSamples)

        // FFT each axis independently
        val xFFT = applyFFT(xWindowed)
        val yFFT = applyFFT(yWindowed)
        val zFFT = applyFFT(zWindowed)

        // Sum power spectra across axes and analyze
        val analysisResult = computeSpectralPowerRatioMultiAxis(xFFT, yFFT, zFFT, fftSize, actualSampleRate)

        Log.d(TAG, "FFT result: powerRatio=${analysisResult.powerRatio}, dominantFreq=${analysisResult.dominantFreq}Hz, totalPower=${analysisResult.totalPower}, sampleRate=$actualSampleRate")

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
        val totalPower: Float
    )

    /**
     * Computes the actual sample rate from timestamps in the raw data.
     * Falls back to DEFAULT_SAMPLE_RATE if insufficient data or timestamps are unreliable.
     */
    private fun computeSampleRate(samples: List<Acceleration>): Float {
        if (samples.size < MIN_SAMPLES_FOR_RATE) return DEFAULT_SAMPLE_RATE

        val firstTimestamp = samples.first().timestamp
        val lastTimestamp = samples.last().timestamp
        val durationMs = lastTimestamp - firstTimestamp

        if (durationMs <= 0) return DEFAULT_SAMPLE_RATE

        val rate = (samples.size - 1) * 1000f / durationMs

        // Sanity check: Android sensors typically run 20–200 Hz
        return if (rate in 20f..200f) rate else DEFAULT_SAMPLE_RATE
    }

    /**
     * Computes summed power spectrum across 3 axes, then evaluates
     * tremor band power ratio and dominant frequency.
     * Summing per-axis power is orientation-invariant and preserves the true tremor frequency.
     */
    private fun computeSpectralPowerRatioMultiAxis(
        xFFT: FloatArray,
        yFFT: FloatArray,
        zFFT: FloatArray,
        fftSize: Int,
        sampleRate: Float
    ): SpectralResult {
        val n = xFFT.size / 2
        val freqResolution = sampleRate / fftSize

        var tremorBandPower = 0f
        var totalPower = 0f
        var peakPower = 0f
        var dominantFreq = 0f

        for (i in 1 until n) {
            val frequency = i * freqResolution
            if (frequency > sampleRate / 2) break // Nyquist

            // Sum power across all three axes for this frequency bin
            val xReal = xFFT[i * 2]
            val xImag = xFFT[i * 2 + 1]
            val yReal = yFFT[i * 2]
            val yImag = yFFT[i * 2 + 1]
            val zReal = zFFT[i * 2]
            val zImag = zFFT[i * 2 + 1]

            val power = xReal.pow(2) + xImag.pow(2) +
                        yReal.pow(2) + yImag.pow(2) +
                        zReal.pow(2) + zImag.pow(2)

            // Accumulate total power (0.5–25 Hz, skip DC and very low freq)
            if (frequency in 0.5f..25f) {
                totalPower += power
            }

            // Accumulate tremor band power
            if (frequency in TREMOR_FREQ_LOW..TREMOR_FREQ_HIGH) {
                tremorBandPower += power
                if (power > peakPower) {
                    peakPower = power
                    dominantFreq = frequency
                }
            }
        }

        val ratio = if (totalPower > 0f) tremorBandPower / totalPower else 0f
        return SpectralResult(ratio, dominantFreq, totalPower)
    }
}
