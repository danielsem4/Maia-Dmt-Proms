package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.Serializable
import maia.dmt.core.data.dto.EvaluationDetailStringDto

@Serializable
data class EvaluationResultDto(
    val clinicId: String,
    val date: String,
    val evaluation: Int,
    val patient_id: String,
    val results: ArrayList<EvaluationDetailStringDto> = arrayListOf()
)