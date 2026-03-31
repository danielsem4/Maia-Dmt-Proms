package maia.dmt.medication.domain.models


data class MedicationNotification(
    val clinic_id: String,
    val patient_id: String,
    val medication_id: String,
    val frequency: String,
    val start_date: String,
    val end_date: String,
    val start_time: String,
)
