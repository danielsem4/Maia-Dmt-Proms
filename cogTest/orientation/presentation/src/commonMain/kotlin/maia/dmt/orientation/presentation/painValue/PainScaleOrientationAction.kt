package maia.dmt.orientation.presentation.painValue

sealed interface PainScaleOrientationAction {
    data class OnPainLevelChange(val level: Int) : PainScaleOrientationAction
    data object OnPlayAudioClick : PainScaleOrientationAction
    data object OnAudioPlaybackComplete : PainScaleOrientationAction

    data object OnNextClick : PainScaleOrientationAction
    data object OnBackClick : PainScaleOrientationAction
    data object OnBackToTask : PainScaleOrientationAction
    data object OnDismissInactivityDialog : PainScaleOrientationAction
}