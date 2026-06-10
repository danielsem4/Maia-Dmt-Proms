package maia.dmt.onoffstate.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnOffStateServerRequest(
    @SerialName("patient_id") val patientId: Int,
    @SerialName("clinic_id") val clinicId: Int,
    @SerialName("upload_date") val uploadDate: String,
    @SerialName("event_type") val eventType: String = "ON_OFF_STATE",
    @SerialName("state") val state: String,
    @SerialName("confidence") val confidence: Float,
    @SerialName("avg_accel_magnitude") val avgAccelMagnitude: Float,
    @SerialName("accel_variability") val accelVariability: Float,
    @SerialName("avg_gyro_magnitude") val avgGyroMagnitude: Float,
    @SerialName("step_frequency") val stepFrequency: Float,
    @SerialName("window_duration_ms") val windowDurationMs: Long
)
