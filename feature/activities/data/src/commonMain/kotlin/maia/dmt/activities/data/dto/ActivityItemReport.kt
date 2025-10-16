package maia.dmt.activities.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityItemReportDto(
    val clinic_id: Int,
    val patient_id: Int,
    val activity_id: Int,
    val date: String
)
