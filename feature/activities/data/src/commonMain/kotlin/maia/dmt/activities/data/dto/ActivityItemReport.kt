package maia.dmt.activities.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityItemReportDto(
    val clinic_id: String,
    val patient_id: String,
    val activity_id: Int,
    val date: String
)
