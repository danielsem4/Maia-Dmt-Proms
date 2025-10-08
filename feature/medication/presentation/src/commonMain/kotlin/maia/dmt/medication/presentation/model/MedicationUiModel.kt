package maia.dmt.medication.presentation.model

data class MedicationUiModel(
    val text: String,
    val id: Int,
    val onClick: () -> Unit
)
