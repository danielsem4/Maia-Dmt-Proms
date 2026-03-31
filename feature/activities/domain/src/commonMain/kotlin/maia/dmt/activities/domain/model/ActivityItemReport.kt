package maia.dmt.activities.domain.model

data class ActivityItemReport(
    val clinic_id: String,
    val patient_id: String,
    val activity_id: Int,
    val date: String
)
