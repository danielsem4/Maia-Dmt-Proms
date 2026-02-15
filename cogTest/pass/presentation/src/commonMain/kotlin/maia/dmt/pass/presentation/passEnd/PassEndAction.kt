package maia.dmt.pass.presentation.passEnd

sealed interface PassEndAction {
    data object OnFinishClick : PassEndAction
    data object OnAudioFinished : PassEndAction
}