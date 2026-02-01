package maia.dmt.core.data.sensors.analysis

import maia.dmt.core.data.sensors.util.SensorMathUtils
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.SensorsData
import org.jtransforms.fft.FloatFFT_1D
import kotlin.math.pow
import kotlin.math.sqrt

class TremorAnalysisUseCase {

    private val SAMPLE_RATE = 50f
    private val MIN_FREQUENCY_AMPLITUDE = 1.5f
    private val THRESHOLD = 0.5f
    private val MIN_TREMOR_DURATION = 15000L
    private var tremorStartTime: Long = 0L

    /**
     * Checks if the phone is stable (on table) or in a pocket.
     * Uses AVERAGED data to avoid noise triggering the check.
     */
    fun isPhoneStableOrInPocket(
        accelX: Float, accelY: Float, accelZ: Float,
        gyroX: Float, gyroY: Float, gyroZ: Float,
        lightLux: Float
    ): Boolean {
        val accelMag = SensorMathUtils.calculateMagnitude(accelX, accelY, accelZ)
        val gyroMag = SensorMathUtils.calculateMagnitude(gyroX, gyroY, gyroZ)

        // Low movement check (On Table)
        if (accelMag < 0.1f && gyroMag < 1.0f) {
            return true
        }

        // Dark environment check (In Pocket)
        if (lightLux < 10.0f) {
            return true
        }

        return false
    }

    /**
     * Main Analysis Function
     * @param accelBuffer: List of 30 AVERAGED items (for Stats/Stability)
     * @param rawAccelList: List of ~128 RAW items (for Frequency/FFT)
     */
    fun analyze(
        accelBuffer: List<Acceleration>,
        gyroBuffer: List<Gyroscope>,
        stepBuffer: List<Long>,
        lightBuffer: List<Float>,
        deletionBuffer: List<Int>,
        rawAccelList: List<Acceleration>
    ): SensorsData? {

        if (accelBuffer.size < 30 || rawAccelList.size < 60) return null


        val avgX = accelBuffer.map { it.x }.average().toFloat()
        val avgY = accelBuffer.map { it.y }.average().toFloat()
        val avgZ = accelBuffer.map { it.z }.average().toFloat()
        val avgLight = lightBuffer.average().toFloat()

        val avgGyroX = gyroBuffer.map { it.x }.average().toFloat()
        val avgGyroY = gyroBuffer.map { it.y }.average().toFloat()
        val avgGyroZ = gyroBuffer.map { it.z }.average().toFloat()

        if (isPhoneStableOrInPocket(avgX, avgY, avgZ, avgGyroX, avgGyroY, avgGyroZ, avgLight)) {
            return null
        }

        val stdDevX = SensorMathUtils.calculateStandardDeviation(accelBuffer.map { it.x })
        val stdDevZ = SensorMathUtils.calculateStandardDeviation(accelBuffer.map { it.z })
        val stdDevSteps = SensorMathUtils.calculateStandardDeviation(stepBuffer.map { it.toFloat() })

        val deletionsFloats = deletionBuffer.map { it.toFloat() }
        val stdDevDeletions = SensorMathUtils.calculateStandardDeviation(deletionsFloats)
        val rangeDeletions = deletionsFloats

        val rangeX = SensorMathUtils.calculateRange(accelBuffer.map { it.x })
        val rangeZ = SensorMathUtils.calculateRange(accelBuffer.map { it.z })
        val rangeGyroX = SensorMathUtils.calculateRange(gyroBuffer.map { it.x })
        val rangeGyroZ = SensorMathUtils.calculateRange(gyroBuffer.map { it.z })

        val steps = stepBuffer.map { it.toFloat() }


        var avgFrequency = 0f

        val fftDataX = applyFFT(rawAccelList.map { it.x })
        val fftDataY = applyFFT(rawAccelList.map { it.y })
        val fftDataZ = applyFFT(rawAccelList.map { it.z })

        val dominantFrequenciesX = findDominantFrequencies(fftDataX)
        val dominantFrequenciesY = findDominantFrequencies(fftDataY)
        val dominantFrequenciesZ = findDominantFrequencies(fftDataZ)

        val allDominantFrequencies = dominantFrequenciesX + dominantFrequenciesY + dominantFrequenciesZ

        avgFrequency = if (allDominantFrequencies.isNotEmpty()) {
            allDominantFrequencies.average().toFloat()
        } else {
            0f
        }

        if (avgFrequency !in 4.0..6.0) {
            avgFrequency = 0f
        }

        val (isDetected, _) = isParkinsonsDetected(
            avgFrequency = avgFrequency,
            stdDevX = stdDevX,
            stdDevZ = stdDevZ,
            rangeX = rangeX,
            rangeZ = rangeZ,
            rangeGyroX = rangeGyroX,
            rangeGyroZ = rangeGyroZ,
            steps = steps,
            stdDevSteps = stdDevSteps
        )

        if (!isDetected) {
            return null
        }

        return SensorsData(
            avgFrequency = avgFrequency,
            stdDevX = stdDevX,
            stdDevZ = stdDevZ,
            threshold = THRESHOLD,
            rangeX = rangeX,
            rangeZ = rangeZ,
            rangeGyroX = rangeGyroX,
            rangeGyroZ = rangeGyroZ,
            steps = steps,
            stdDevSteps = stdDevSteps,
            stdDevDeletions = stdDevDeletions,
            rangeDeletions = rangeDeletions
        )
    }

    private fun isParkinsonsDetected(
        avgFrequency: Float,
        stdDevX: Float,
        stdDevZ: Float,
        rangeX: Float,
        rangeZ: Float,
        rangeGyroX: Float,
        rangeGyroZ: Float,
        steps: List<Float>,
        stdDevSteps: Float,
    ): Pair<Boolean, Long> {
        val isDetected = avgFrequency in 4.0..6.0 &&
                (stdDevX > THRESHOLD && stdDevZ > THRESHOLD) &&
                (rangeX > 1.5f || rangeZ > 1.5f || rangeGyroX > 1.0f || rangeGyroZ > 1.0f) &&
                (steps.average() > 100 && stdDevSteps > 0.2f)

        if (isDetected) {
            if (tremorStartTime == 0L) {
                tremorStartTime = System.currentTimeMillis()
                return true to tremorStartTime
            } else {
                val currentTime = System.currentTimeMillis()
                if (currentTime - tremorStartTime >= MIN_TREMOR_DURATION) {
                    return true to tremorStartTime
                }
            }
        } else {
            tremorStartTime = 0L
        }
        return false to 0L
    }

    private fun applyFFT(data: List<Float>): FloatArray {
        val fft = FloatFFT_1D(data.size.toLong())
        val fftData = FloatArray(data.size * 2)
        for (i in data.indices) {
            fftData[i] = data[i]
        }
        fft.realForward(fftData)
        return fftData
    }

    private fun findDominantFrequencies(fftData: FloatArray): List<Float> {
        val frequencies = mutableListOf<Float>()
        val n = fftData.size / 2

        for (i in 1 until n) {
            val frequency = i * SAMPLE_RATE / (n * 2)

            val real = fftData[i * 2]
            val imag = fftData[i * 2 + 1]
            val amplitude = sqrt(real.pow(2) + imag.pow(2))

            if (frequency in 4.0..6.0 && amplitude > MIN_FREQUENCY_AMPLITUDE) {
                frequencies.add(frequency)
            }
        }
        return frequencies
    }
}