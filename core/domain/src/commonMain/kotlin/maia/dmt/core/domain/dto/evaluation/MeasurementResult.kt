package maia.dmt.core.domain.dto.evaluation

import maia.dmt.core.domain.dto.MeasurementDetailString

data class MeasurementResult(
    val clinicId: Int,
    val date: String,
    val measurement: Int,
    val patientId: Int,
    val results: ArrayList<MeasurementDetailString> = arrayListOf()
)