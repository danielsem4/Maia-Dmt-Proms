package maia.dmt.core.data.sensors.util

import kotlin.math.pow
import kotlin.math.sqrt

object SensorMathUtils {

    fun calculateMagnitude(x: Float, y: Float, z: Float): Float {
        return sqrt(x * x + y * y + z * z)
    }

    fun processGyroscope(x: Float, y: Float, z: Float): Triple<Float, Float, Float> {
        return Triple(x, y, z)
    }

    fun calculateStandardDeviation(data: List<Float>): Float {
        if (data.isEmpty()) return 0.0f
        val mean = data.average().toFloat()
        val variance = data.map { (it - mean).pow(2) }.average().toFloat()
        return sqrt(variance)
    }

    fun calculateRange(data: List<Float>): Float {
        if (data.isEmpty()) return 0f
        val max = data.maxOrNull() ?: 0f
        val min = data.minOrNull() ?: 0f
        return max - min
    }

    fun applyHanningWindow(data: FloatArray): FloatArray {
        val n = data.size
        val windowed = FloatArray(n)
        for (i in 0 until n) {
            val multiplier = 0.5f * (1f - kotlin.math.cos(2.0 * Math.PI * i / (n - 1)).toFloat())
            windowed[i] = data[i] * multiplier
        }
        return windowed
    }
}