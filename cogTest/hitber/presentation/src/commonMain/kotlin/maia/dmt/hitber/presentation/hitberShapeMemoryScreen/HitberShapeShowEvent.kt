package maia.dmt.hitber.presentation.hitberShapeMemoryScreen

interface HitberShapeShowEvent {
    data object OnDialogDismiss : HitberShapeShowEvent
    data object OnConfirmDialog : HitberShapeShowEvent
    data object OnNextClick : HitberShapeShowEvent
}