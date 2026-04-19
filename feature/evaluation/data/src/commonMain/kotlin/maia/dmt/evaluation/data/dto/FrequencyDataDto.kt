package maia.dmt.evaluation.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FrequencyDataDto(
    val date: String? = null,
    @SerialName("time_slots")
    val timeSlots: List<String> = emptyList(),
    @SerialName("times_per_day")
    val timesPerDay: Int? = null,
    val days: List<String>? = null
)
