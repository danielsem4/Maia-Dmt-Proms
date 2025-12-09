package maia.dmt.cdt.presentation.cdtGrade

sealed interface CdtGradeAction {
    data class OnCircleGradeSelected(val grade: String) : CdtGradeAction
    data class OnNumbersGradeSelected(val grade: String) : CdtGradeAction
    data class OnHandsGradeSelected(val grade: String) : CdtGradeAction
    data object OnSaveClick : CdtGradeAction
}