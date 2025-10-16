package maia.dmt.core.data.mapper

import maia.dmt.core.data.dto.evaluation.EvaluationDto
import maia.dmt.core.data.dto.evaluation.EvaluationObjectDto
import maia.dmt.core.data.dto.evaluation.EvaluationSettingsDto
import maia.dmt.core.data.dto.evaluation.EvaluationValueDto
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationSettings
import maia.dmt.core.domain.dto.evaluation.EvaluationValue

fun EvaluationValueDto.toDomain(): EvaluationValue {
    return EvaluationValue(
        id = id,
        available_value = available_value,
        default_value = default_value,
        object_address = object_address,
        measurementObject_id = measurementObject_id
    )
}

fun EvaluationSettingsDto.toDomain(): EvaluationSettings {
    return EvaluationSettings(
        id = id,
        measurement_repeat_period = measurement_repeat_period,
        measurement_repeat_times = measurement_repeat_times,
        measurement_begin_time = measurement_begin_time,
        measurement_last_time = measurement_last_time,
        measurement_end_time = measurement_end_time,
        is_repetitive = is_repetitive,
        times_taken = times_taken,
        patient = patient,
        doctor = doctor,
        clinic = clinic,
        measurement = measurement
    )
}

fun EvaluationObjectDto.toDomain(): EvaluationObject {
    return EvaluationObject(
        id = id,
        object_label = object_label,
        measurement_screen = measurement_screen,
        measurement_order = measurement_order,
        return_value = return_value,
        number_of_values = number_of_values,
        predefined_values = predefined_values,
        random_selection = random_selection,
        order_important = order_important,
        show_icon = show_icon,
        answer = answer,
        style = style,
        is_grade = is_grade,
        object_type = object_type,
        language = language,
        available_values = available_values?.map { it.toDomain() } ?: emptyList()
    )
}

fun EvaluationDto.toDomain(): Evaluation {
    return Evaluation(
        id = id,
        measurement_name = measurement_name,
        display_as_module = display_as_module,
        is_multilingual = is_multilingual,
        is_active = is_active,
        measurement_settings = measurement_settings.toDomain(),
        measurement_objects = measurement_objects.map { it.toDomain() }
    )
}


fun EvaluationValue.toSerial(): EvaluationValueDto {
    return EvaluationValueDto(
        id = id,
        available_value = available_value,
        default_value = default_value,
        object_address = object_address,
        measurementObject_id = measurementObject_id
    )
}

fun EvaluationSettings.toSerial(): EvaluationSettingsDto {
    return EvaluationSettingsDto(
        id = id,
        measurement_repeat_period = measurement_repeat_period,
        measurement_repeat_times = measurement_repeat_times,
        measurement_begin_time = measurement_begin_time,
        measurement_last_time = measurement_last_time,
        measurement_end_time = measurement_end_time,
        is_repetitive = is_repetitive,
        times_taken = times_taken,
        patient = patient,
        doctor = doctor,
        clinic = clinic,
        measurement = measurement
    )
}

fun EvaluationObject.toSerial(): EvaluationObjectDto {
    return EvaluationObjectDto(
        id = id,
        object_label = object_label,
        measurement_screen = measurement_screen,
        measurement_order = measurement_order,
        return_value = return_value,
        number_of_values = number_of_values,
        predefined_values = predefined_values,
        random_selection = random_selection,
        order_important = order_important,
        show_icon = show_icon,
        answer = answer,
        style = style,
        is_grade = is_grade,
        object_type = object_type,
        language = language,
        available_values = available_values?.map { it.toSerial() } ?: emptyList()
    )
}

fun Evaluation.toSerial(): EvaluationDto {
    return EvaluationDto(
        id = id,
        measurement_name = measurement_name,
        display_as_module = display_as_module,
        is_multilingual = is_multilingual,
        is_active = is_active,
        measurement_settings = measurement_settings.toSerial(),
        measurement_objects = measurement_objects.map { it.toSerial() }
    )
}