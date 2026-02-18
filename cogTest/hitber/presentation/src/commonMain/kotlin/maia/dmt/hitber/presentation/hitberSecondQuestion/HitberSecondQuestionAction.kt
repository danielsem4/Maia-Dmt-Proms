package maia.dmt.hitber.presentation.hitberSecondQuestion

import maia.dmt.hitber.domain.model.HitberShape

interface HitberSecondQuestionAction {
    data class OnShapeClick(val shape: HitberShape) : HitberSecondQuestionAction
    data object OnNextClick : HitberSecondQuestionAction
    data object OnErrorDialogDismiss : HitberSecondQuestionAction
}
