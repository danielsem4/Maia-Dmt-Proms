package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportedMedicationDto(
    val id: String = "",
    val patient_medication: String = "",
    val patient: String = "",
    val med_name: String = "",
    val taken_at: String = "",
    val dosage_taken: String = "",
    val status: String = ""
)