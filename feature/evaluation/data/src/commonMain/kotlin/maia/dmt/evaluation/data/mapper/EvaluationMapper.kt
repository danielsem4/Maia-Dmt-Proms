package maia.dmt.evaluation.data.mapper

import maia.dmt.evaluation.data.dto.EvaluationDto
import maia.dmt.evaluation.data.dto.EvaluationObjectDto
import maia.dmt.evaluation.data.dto.EvaluationSettingsDto
import maia.dmt.evaluation.data.dto.EvaluationValueDto
import maia.dmt.evaluation.domain.models.Evaluation
import maia.dmt.evaluation.domain.models.EvaluationObject
import maia.dmt.evaluation.domain.models.EvaluationSettings
import maia.dmt.evaluation.domain.models.EvaluationValue

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