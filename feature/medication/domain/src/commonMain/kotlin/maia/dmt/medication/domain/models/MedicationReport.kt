package maia.dmt.medication.domain.models


data class MedicationReport(
    val clinic_id: String,
    val patient_id: String,
    val medication_id: String,
    val timestamp: String
)
