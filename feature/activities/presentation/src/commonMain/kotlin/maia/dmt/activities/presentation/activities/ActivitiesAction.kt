package maia.dmt.activities.presentation.activities

interface ActivitiesAction {

    data class OnActivitiesClick(val activityId: String): ActivitiesAction
    data object OnBackClick: ActivitiesAction

    data object OnActivitiesReportClick: ActivitiesAction
    data object OnDismissReportDialog: ActivitiesAction

    data object OnConfirmReport: ActivitiesAction
    data object OnDateTimeClick: ActivitiesAction

    data class OnDateSelected(val dateMillis: Long): ActivitiesAction
    data class OnTimeSelected(val hour: Int, val minute: Int): ActivitiesAction
    data object OnDismissDatePicker: ActivitiesAction
    data object OnDismissTimePicker: ActivitiesAction

}