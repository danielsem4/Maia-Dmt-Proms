package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.Serializable
import maia.dmt.core.data.dto.MeasurementDetailStringDto

@Serializable
data class MeasurementResultDto(
    val clinicId: String,
    val date: String,
    val measurement: Int,
    val patient_id: String,
    val results: ArrayList<MeasurementDetailStringDto> = arrayListOf()
)