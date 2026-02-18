package maia.dmt.hitber.presentation.hitberFirstQuestion

interface HitberFirstQuestionAction {
    data object OnNextClick : HitberFirstQuestionAction
    data object OnBackClick : HitberFirstQuestionAction
    data class OnAnswerSelected(val questionId: Int, val answer: String) : HitberFirstQuestionAction
}
