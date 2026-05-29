package maia.dmt.core.domain.sensors.model

enum class AnomalyEventType {
    TREMOR, FALL, NEAR_FALL, ON_OFF_STATE
}

data class AnomalyEvent(
    val eventType: AnomalyEventType,
    val timestamp: Long,
    val rawAccel: List<Acceleration>,
    val rawGyro: List<Gyroscope>,
    val aggregatedStats: SensorsData?
)
