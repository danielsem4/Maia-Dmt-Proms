package maia.dmt.core.domain.model

data class Acceleration(val x: Float, val y: Float, val z: Float, val timestamp: Long)
data class Gyroscope(val x: Float, val y: Float, val z: Float, val timestamp: Long)
data class LightLevel(val lux: Float)
data class StepCount(val steps: Long)


fun Acceleration.magnitude(): Float {
    return kotlin.math.sqrt(x * x + y * y + z * z)
}
