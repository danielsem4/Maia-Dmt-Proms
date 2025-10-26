package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportedMedicationDto(
    val id: String,
    val name: String,
    val form: String,
    val dosage: String,
    val time_taken: String
)