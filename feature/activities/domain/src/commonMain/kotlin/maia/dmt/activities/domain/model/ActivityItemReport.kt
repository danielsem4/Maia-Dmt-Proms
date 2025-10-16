package maia.dmt.activities.domain.model

data class ActivityItemReport(
    val clinic_id: Int,
    val patient_id: Int,
    val activity_id: Int,
    val date: String
)
