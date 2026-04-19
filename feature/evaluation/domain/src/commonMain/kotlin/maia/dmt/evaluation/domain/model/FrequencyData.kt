package maia.dmt.evaluation.domain.model

sealed class FrequencyData {
    data class Once(
        val date: String,
        val timeSlots: List<String>
    ) : FrequencyData()

    data class Daily(
        val timeSlots: List<String>,
        val timesPerDay: Int? = null
    ) : FrequencyData()

    data class Weekly(
        val days: List<String>,
        val timeSlots: List<String>
    ) : FrequencyData()
}
