package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MedicationReportDto(
    val clinic_id: String,
    val patient_id: String,
    val medication_id: String,
    val timestamp: String
)
