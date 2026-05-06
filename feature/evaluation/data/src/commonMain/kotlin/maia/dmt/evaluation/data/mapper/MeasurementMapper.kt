package maia.dmt.evaluation.data.mapper

import maia.dmt.evaluation.data.dto.FrequencyDataDto
import maia.dmt.evaluation.data.dto.MeasurementItemDto
import maia.dmt.evaluation.domain.model.Frequency
import maia.dmt.evaluation.domain.model.FrequencyData
import maia.dmt.evaluation.domain.model.MeasurementItem

fun MeasurementItemDto.toDomain(): MeasurementItem {
    val freq = Frequency.fromString(frequency)
    return MeasurementItem(
        id = id,
        patient = patient,
        clinic = clinic,
        doctor = doctor,
        measurement = measurement,
        measurementName = measurementName,
        measurementType = measurementType,
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
