package maia.dmt.activities.presentation.activities

import maia.dmt.activities.presentation.model.ActivityUiModel
import maia.dmt.core.presentation.util.UiText
import kotlin.time.Clock

data class ActivitiesState(
    val isLoadingActivities: Boolean = false,
    val activitiesError: UiText? = null,
    val allActivities: List<ActivityUiModel> = emptyList(),
    val activities: List<ActivityUiModel> = emptyList(),
    val showActivityReportDialog: Boolean = false,
    val selectedActivity: ActivityUiModel? = null,
    val selectedDateTime: Long = Clock.System.now().toEpochMilliseconds(),
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val isReportingActivity: Boolean = false,
)

