package maia.dmt.core.domain.sensors.model

enum class AnomalyEventType {
    TREMOR, FALL, NEAR_FALL
}

data class AnomalyEvent(
    val eventType: AnomalyEventType,
    val timestamp: Long,
    val rawAccel: List<Acceleration>,
    val rawGyro: List<Gyroscope>,
    val aggregatedStats: SensorsData?
)
