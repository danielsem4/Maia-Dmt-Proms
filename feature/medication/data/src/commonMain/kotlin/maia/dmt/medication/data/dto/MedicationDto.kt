package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MedicationDto(
    val id: String,
    val patient: String,
    val clinic: String,
    val doctor: String,
    val medication: String,
    val med_name: String,
    val med_form: String,
    val med_unit: String,
    val frequency: String?,
    val frequency_data: FrequencyDataDto,
    val start_date: String,
    val end_date: String?,
    val dosage: String
)
