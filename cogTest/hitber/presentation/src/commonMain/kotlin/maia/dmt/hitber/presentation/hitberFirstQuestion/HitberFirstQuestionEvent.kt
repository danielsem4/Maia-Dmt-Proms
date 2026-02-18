package maia.dmt.hitber.presentation.hitberFirstQuestion

interface HitberFirstQuestionEvent {
    data object NavigateToNextScreen : HitberFirstQuestionEvent
    data object NavigateBack : HitberFirstQuestionEvent
    data class ShowToast(val message: String) : HitberFirstQuestionEvent
}
