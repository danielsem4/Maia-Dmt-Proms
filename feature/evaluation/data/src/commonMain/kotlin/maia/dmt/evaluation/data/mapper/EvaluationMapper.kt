package maia.dmt.evaluation.data.mapper

import maia.dmt.evaluation.data.dto.FrequencyDataDto
import maia.dmt.evaluation.data.dto.EvaluationItemDto
import maia.dmt.evaluation.domain.model.Frequency
import maia.dmt.evaluation.domain.model.FrequencyData
import maia.dmt.evaluation.domain.model.EvaluationItem

fun EvaluationItemDto.toDomain(): EvaluationItem {
    val freq = Frequency.fromString(frequency)
    return EvaluationItem(
        id = id,
        patient = patient,
        clinic = clinic,
        doctor = doctor,
        evaluation = evaluation,
        evaluationName = evaluationName,
        evaluationType = evaluationType,
        startDate = startDate,
        endDate = endDate,
        frequency = freq,
        frequencyData = frequencyData.toDomain(freq)
    )
}

fun FrequencyDataDto.toDomain(frequency: Frequency): FrequencyData {
    return when (frequency) {
        Frequency.ONCE -> FrequencyData.Once(
            date = date ?: "",
            timeSlots = timeSlots
        )
        Frequency.DAILY -> FrequencyData.Daily(
            timeSlots = timeSlots,
            timesPerDay = timesPerDay
        )
        Frequency.WEEKLY -> FrequencyData.Weekly(
            days = days ?: emptyList(),
            timeSlots = timeSlots
        )
    }
}
