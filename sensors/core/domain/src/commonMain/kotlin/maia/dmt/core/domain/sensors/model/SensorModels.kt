package maia.dmt.core.domain.sensors.model

import kotlin.math.sqrt

data class Acceleration(val x: Float, val y: Float, val z: Float, val timestamp: Long)
data class Gyroscope(val x: Float, val y: Float, val z: Float, val timestamp: Long)
data class LightLevel(val lux: Float)
data class StepCount(val steps: Long)


fun Acceleration.magnitude(): Float {
    return sqrt(x * x + y * y + z * z)
}
