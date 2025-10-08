package maia.dmt.medication.domain.models


data class MedicationReport(
    val clinic_id: Int,
    val patient_id: Int,
    val medication_id: Int,
    val timestamp: String
)
