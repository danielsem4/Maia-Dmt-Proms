package maia.dmt.medication.domain.models

data class ReportedMedication(
    val id: String,
    val patientMedication: String,
    val medName: String,
    val takenAt: String,
    val dosageTaken: String,
    val status: String
)
