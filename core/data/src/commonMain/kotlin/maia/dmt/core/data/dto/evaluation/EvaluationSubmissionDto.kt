package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EvaluationSubmissionRequestDto(
    @SerialName("version_key") val versionKey: String? = null,
    val answers: List<EvaluationAnswerDto>
)

@Serializable
data class EvaluationAnswerDto(
    @SerialName("element_id") val elementId: String,
    val value: JsonElement,
    @SerialName("points_earned") val pointsEarned: Int? = null
)
