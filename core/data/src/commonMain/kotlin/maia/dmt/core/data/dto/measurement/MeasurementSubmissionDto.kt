package maia.dmt.core.data.dto.measurement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MeasurementSubmissionRequestDto(
    val answers: List<SubmissionAnswerDto>
)

@Serializable
data class SubmissionAnswerDto(
    @SerialName("element_id")
    val elementId: String,
    val value: JsonElement,
    @SerialName("points_earned")
    val pointsEarned: Int? = null
)
