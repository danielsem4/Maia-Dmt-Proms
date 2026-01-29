package maia.dmt.core.data.sensors.util

import kotlin.math.pow
import kotlin.math.sqrt

object SensorMathUtils {
    private const val ALPHA = 0.8f

    fun processAccelerometer(
        x: Float, y: Float, z: Float,
        gravity: FloatArray,
        linearAcceleration: FloatArray
    ): Triple<Float, Float, Float> {
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * x
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * y
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * z

        linearAcceleration[0] = x - gravity[0]
        linearAcceleration[1] = y - gravity[1]
        linearAcceleration[2] = z - gravity[2]

        return Triple(linearAcceleration[0], linearAcceleration[1], linearAcceleration[2])
    }

    fun calculateMagnitude(x: Float, y: Float, z: Float): Float {
        return sqrt(x * x + y * y + z * z)
    }

    fun processGyroscope(x: Float, y: Float, z: Float): Triple<Float, Float, Float> {
        val magnitude = sqrt(x * x + y * y + z * z)
        return if (magnitude > 0) {
            Triple(x / magnitude, y / magnitude, z / magnitude)
        } else {
            Triple(0f, 0f, 0f)
        }
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
}