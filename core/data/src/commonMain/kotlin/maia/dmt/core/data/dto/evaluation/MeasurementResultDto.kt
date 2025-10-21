package maia.dmt.core.data.dto.evaluation

import kotlinx.serialization.Serializable
import maia.dmt.core.data.dto.MeasurementDetailStringDto

@Serializable
data class MeasurementResultDto(
    val clinicId: Int,
    val date: String,
    val measurement: Int,
    val patient_id: Int,
    val results: ArrayList<MeasurementDetailStringDto> = arrayListOf()
)