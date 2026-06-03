package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class EvaluationStructureDto(
    @SerialName("evaluation_id")
    val evaluationId: String,
    @SerialName("evaluation_name")
    val evaluationName: String,
    val screens: List<EvaluationScreenDto>
)

@Serializable
data class EvaluationScreenDto(
    val id: String,
    @SerialName("screen_number")
    val screenNumber: Int,
    val title: String,
    val rows: List<EvaluationRowDto>
)

@Serializable
data class EvaluationRowDto(
    @SerialName("row_number")
    val rowNumber: Int,
    val elements: List<EvaluationElementDto>
)

@Serializable
data class EvaluationElementDto(
    val id: String,
    @SerialName("element_type")
    val elementType: String,
    @SerialName("row_number")
    val rowNumber: Int,
    @SerialName("order_in_row")
    val orderInRow: Int,
    val label: String,
    @SerialName("is_required")
    val isRequired: Boolean,
    val config: JsonObject = JsonObject(emptyMap())
)
