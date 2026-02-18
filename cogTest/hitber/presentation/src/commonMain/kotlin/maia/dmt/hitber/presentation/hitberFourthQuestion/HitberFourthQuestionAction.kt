package maia.dmt.hitber.presentation.hitberFourthQuestion

interface HitberFourthQuestionAction {
    data class OnWordSelected(val word: HitberWord) : HitberFourthQuestionAction
    data object OnNextClick : HitberFourthQuestionAction
}
