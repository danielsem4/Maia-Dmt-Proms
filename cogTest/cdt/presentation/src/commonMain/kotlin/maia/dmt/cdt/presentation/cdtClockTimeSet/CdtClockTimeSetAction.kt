package maia.dmt.cdt.presentation.cdtClockTimeSet

sealed interface CdtClockTimeSetAction {
    data class OnHourHandRotated(val angle: Float) : CdtClockTimeSetAction
    data class OnMinuteHandRotated(val angle: Float) : CdtClockTimeSetAction
    data object OnNextClick : CdtClockTimeSetAction
    data object OnResetClick : CdtClockTimeSetAction
    data object OnBackClick : CdtClockTimeSetAction
}