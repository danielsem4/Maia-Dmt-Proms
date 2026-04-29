package maia.dmt.medication.domain.models


data class MedicationReport(
    val taken_at: String,
    val dosage_taken: String,
    val status: String
)
