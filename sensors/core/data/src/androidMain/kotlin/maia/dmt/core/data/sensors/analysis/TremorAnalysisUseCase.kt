package maia.dmt.core.data.sensors.analysis

import maia.dmt.core.data.sensors.util.SensorMathUtils
import maia.dmt.core.domain.sensors.model.Acceleration
import maia.dmt.core.domain.sensors.model.Gyroscope
import maia.dmt.core.domain.sensors.model.SensorsData
import org.jtransforms.fft.FloatFFT_1D
import kotlin.math.pow
import kotlin.math.sqrt

class TremorAnalysisUseCase {

    private val MIN_FREQUENCY_AMPLITUDE = 1.5f
    private val MIN_TREMOR_COUNT = 3
    private val SAMPLE_RATE = 50f
    private val BUFFER_SIZE = 150
    private val THRESHOLD = 0.5f
    private val MIN_TREMOR_DURATION = 5000L

    private val frequencyData = mutableListOf<Float>()
    private var lastDetectionTime: Long = 0L
    private var tremorStartTime: Long = 0L
    /**
     * Helper to skip processing if phone is stable or in pocket.
     */
    fun isPhoneStableOrInPocket(
        accelX: Float, accelY: Float, accelZ: Float,
        gyroX: Float, gyroY: Float, gyroZ: Float,
        lightLux: Float
    ): Boolean {
        // 1. Check Table (Low movement)
        val accelMag = SensorMathUtils.calculateMagnitude(accelX, accelY, accelZ)
        val gyroMag = SensorMathUtils.calculateMagnitude(gyroX, gyroY, gyroZ)

        if (accelMag < 0.1f && gyroMag < 1.0f) {
            // Phone is sitting still on a table
            return true
        }

        // 2. Check Pocket (Darkness)
        if (lightLux < 10.0f) {
            // Phone is likely in a pocket
            return true
        }

        return false
    }

    /**
     * Main Analysis Function.
     * Called continuously by the Service.
     * * 1. Calculates Instant Frequency.
     * 2. Accumulates Frequency Data.
     * 3. When buffer is full -> Runs FFT + Statistical Analysis on the raw buffers.
     * 4. Returns SensorsData (Domain Model) if Parkinson's is detected.
     */
    fun analyze(
        accelBuffer: List<Acceleration>,
        gyroBuffer: List<Gyroscope>,
        stepBuffer: List<Long>,
        timestamp: Long
    ): SensorsData? {
        val prevTimestamp = if (lastDetectionTime != 0L) lastDetectionTime else timestamp
        val timeInterval = (timestamp - prevTimestamp) / 1000.0f
        val frequency = if (timeInterval > 0) 1.0f / timeInterval else 0f
        lastDetectionTime = timestamp
        frequencyData.add(frequency)

        if (frequencyData.size >= BUFFER_SIZE) {

            val fftData = applyFFT(frequencyData)
            val dominantFreqs = findDominantFrequencies(fftData)
            val avgFrequency = if (dominantFreqs.isNotEmpty()) dominantFreqs.average().toFloat() else 0f

            frequencyData.clear()

            val samplesX = accelBuffer.map { it.x }
            val samplesZ = accelBuffer.map { it.z }
            val gyroX = gyroBuffer.map { it.x }
            val gyroZ = gyroBuffer.map { it.z }
            val stepFloats = stepBuffer.map { it.toFloat() }

            val stdDevX = SensorMathUtils.calculateStandardDeviation(samplesX)
            val stdDevZ = SensorMathUtils.calculateStandardDeviation(samplesZ)
            val rangeX = SensorMathUtils.calculateRange(samplesX)
            val rangeZ = SensorMathUtils.calculateRange(samplesZ)
            val rangeGyroX = SensorMathUtils.calculateRange(gyroX)
            val rangeGyroZ = SensorMathUtils.calculateRange(gyroZ)
            val stdDevSteps = SensorMathUtils.calculateStandardDeviation(stepFloats)

            val isFreqInRange = avgFrequency in 4.0..6.0
            val isHighStdDev = (stdDevX > THRESHOLD && stdDevZ > THRESHOLD)
            val isHighRange = (rangeX > 1.5f || rangeZ > 1.5f || rangeGyroX > 1.0f || rangeGyroZ > 1.0f)
            val isActive = (stepFloats.average() > 0 && stdDevSteps > 0.2f)

            val isDetected = isFreqInRange && isHighStdDev && isHighRange && isActive

            if (isDetected) {
                val currentTime = System.currentTimeMillis()
                if (tremorStartTime == 0L) tremorStartTime = currentTime

                if (currentTime - tremorStartTime >= MIN_TREMOR_DURATION) {

                    return SensorsData(
                        avgFrequency = avgFrequency,
                        stdDevX = stdDevX,
                        stdDevZ = stdDevZ,
                        threshold = THRESHOLD,
                        rangeX = rangeX,
                        rangeZ = rangeZ,
                        rangeGyroX = rangeGyroX,
                        rangeGyroZ = rangeGyroZ,
                        steps = stepFloats,
                        stdDevSteps = stdDevSteps,
                    )
                }
            } else {
                tremorStartTime = 0L
            }
        }
        return null
    }

    private fun applyFFT(data: List<Float>): FloatArray {
        val fft = FloatFFT_1D(data.size.toLong())
        val fftData = data.toFloatArray()
        fft.realForward(fftData)
        return fftData
    }

    private fun findDominantFrequencies(fftData: FloatArray): List<Float> {
        val frequencies = mutableListOf<Float>()
        val halfSize = fftData.size / 2

        for (i in 1 until halfSize) {
            val frequency = i * SAMPLE_RATE / fftData.size
            val real = fftData[i * 2]
            val imag = fftData[i * 2 + 1]
            val amplitude = sqrt(real.pow(2) + imag.pow(2))

            if (frequency in 4.0..6.0 && amplitude > MIN_FREQUENCY_AMPLITUDE) {
                frequencies.add(frequency)
            }
        }
        return frequencies
    }

    data class TremorResult(val isTremor: Boolean, val frequency: Float)
}