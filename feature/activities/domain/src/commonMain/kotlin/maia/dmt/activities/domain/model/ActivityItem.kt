package maia.dmt.activities.domain.model

data class ActivityItem(
    val id: String,
    val patient: String,
    val clinic: String,
    val doctor: String,
    val activity: String,
    val activityName: String,
    val activityDescription: String,
    val startDate: String?,
    val endDate: String?,
    val frequency: String?,
    val frequencyData: String?
)
