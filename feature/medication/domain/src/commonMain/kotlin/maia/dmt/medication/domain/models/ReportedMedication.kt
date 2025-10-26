package maia.dmt.medication.domain.models

data class ReportedMedication(
    val id: String,
    val name: String,
    val form: String,
    val dosage: String,
    val time_taken: String
)
