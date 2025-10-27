package maia.dmt.medication.presentation.model

data class ReportedMedicationUiModel(
    val id: String,
    val name: String,
    val form: String,
    val dosage: String,
    val date: String
)
