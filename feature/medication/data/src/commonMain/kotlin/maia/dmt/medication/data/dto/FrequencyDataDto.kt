package maia.dmt.medication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FrequencyDataDto(
    val time_slots: List<String>,
    val times_per_day: Int
)
