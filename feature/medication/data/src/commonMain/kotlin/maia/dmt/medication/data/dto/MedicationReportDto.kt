package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MedicationReportDto(
    val taken_at: String,
    val dosage_taken: String,
    val status: String
)
