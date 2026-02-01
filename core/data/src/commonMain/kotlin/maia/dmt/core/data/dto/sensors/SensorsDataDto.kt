package maia.dmt.core.data.dto.sensors

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SensorsDataServerRequest(
    @SerialName("patient_id") val patientId: Int,
    @SerialName("clinic_id") val clinicId: Int,
    @SerialName("upload_date") val uploadDate: String,
    @SerialName("data") val data: SensorsDataDto
)

@Serializable
data class SensorsDataDto(
    val avgFrequency: Float,
    val stdDevX: Float,
    val stdDevZ: Float,
    @SerialName("THRESHOLD") val threshold: Float,
    val rangeX: Float,
    val rangeZ: Float,
    val rangeGyroX: Float,
    val rangeGyroZ: Float,
    val steps: List<Float>,
    val stdDevSteps: Float,
    val stdDevDeletions: Float,
    val rangeDeletions: List<Float>
)