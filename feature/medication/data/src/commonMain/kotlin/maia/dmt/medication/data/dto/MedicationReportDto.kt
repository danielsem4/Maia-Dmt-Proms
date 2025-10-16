package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MedicationReportDto(
    val clinic_id: Int,
    val patient_id: Int,
    val medication_id: String,
    val timestamp: String
)
