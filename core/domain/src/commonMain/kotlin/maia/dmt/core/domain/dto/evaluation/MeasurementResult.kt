package maia.dmt.core.domain.dto.evaluation

import maia.dmt.core.domain.dto.MeasurementDetailString

data class MeasurementResult(
    val clinicId: String,
    val date: String,
    val measurement: Int,
    val patientId: String,
    val results: ArrayList<MeasurementDetailString> = arrayListOf()
)