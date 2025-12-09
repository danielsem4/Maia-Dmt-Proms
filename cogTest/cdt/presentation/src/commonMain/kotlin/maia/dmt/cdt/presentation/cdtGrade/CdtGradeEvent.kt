package maia.dmt.cdt.presentation.cdtGrade

sealed interface CdtGradeEvent {
    data object NavigateBack : CdtGradeEvent
}