package maia.dmt.activities.presentation.activities

interface ActivitiesEvent {

    data object NavigateBack: ActivitiesEvent
    data object ReportActivitiesSuccess : ActivitiesEvent
    data class ReportActivitiesError(val message: String?) : ActivitiesEvent
    
}