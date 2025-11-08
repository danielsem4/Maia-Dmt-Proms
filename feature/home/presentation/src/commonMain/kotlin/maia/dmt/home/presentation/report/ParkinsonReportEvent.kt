package maia.dmt.home.presentation.report

sealed interface ParkinsonReportEvent {
    data object SubmitSuccess : ParkinsonReportEvent
    data class SubmitError(val message: String?) : ParkinsonReportEvent
}