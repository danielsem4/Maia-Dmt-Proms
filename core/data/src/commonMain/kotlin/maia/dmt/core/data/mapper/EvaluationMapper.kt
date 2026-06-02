package maia.dmt.core.data.mapper

import maia.dmt.core.data.dto.EvaluationDetailDto
import maia.dmt.core.data.dto.EvaluationDetailGenericDto
import maia.dmt.core.data.dto.EvaluationDetailIntDto
import maia.dmt.core.data.dto.EvaluationDetailStringDto
import maia.dmt.core.data.dto.evaluation.EvaluationDto
import maia.dmt.core.data.dto.evaluation.EvaluationObjectDto
import maia.dmt.core.data.dto.evaluation.EvaluationSettingsDto
import maia.dmt.core.data.dto.evaluation.EvaluationValueDto
import maia.dmt.core.data.dto.evaluation.EvaluationResultDto
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.data.mapper.toDto
import maia.dmt.core.domain.dto.EvaluationDetail
import maia.dmt.core.domain.dto.EvaluationDetailGeneric
import maia.dmt.core.domain.dto.EvaluationDetailInt
import maia.dmt.core.domain.dto.EvaluationDetailString
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.dto.evaluation.EvaluationObject
import maia.dmt.core.domain.dto.evaluation.EvaluationSettings
import maia.dmt.core.domain.dto.evaluation.EvaluationValue
import maia.dmt.core.domain.dto.evaluation.EvaluationResult

fun EvaluationValueDto.toDomain(): EvaluationValue {
    return EvaluationValue(
        id = id,
        available_value = available_value,
        default_value = default_value,
        object_address = object_address,
        evaluationObject_id = evaluationObject_id
    )
}

fun EvaluationSettingsDto.toDomain(): EvaluationSettings {
    return EvaluationSettings(
        id = id,
        evaluation_repeat_period = evaluation_repeat_period,
        evaluation_repeat_times = evaluation_repeat_times,
        evaluation_begin_time = evaluation_begin_time,
        evaluation_last_time = evaluation_last_time,
        evaluation_end_time = evaluation_end_time,
        is_repetitive = is_repetitive,
        times_taken = times_taken,
        patient = patient,
        doctor = doctor,
        clinic = clinic,
        evaluation = evaluation
    )
}

fun EvaluationObjectDto.toDomain(): EvaluationObject {
    return EvaluationObject(
        id = id,
        object_label = object_label,
        evaluation_screen = evaluation_screen,
        evaluation_order = evaluation_order,
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
        evaluation_name = evaluation_name,
        display_as_module = display_as_module,
        is_multilingual = is_multilingual,
        is_active = is_active,
        evaluation_settings = evaluation_settings.toDomain(),
        evaluation_objects = evaluation_objects.map { it.toDomain() }
    )
}


fun EvaluationValue.toSerial(): EvaluationValueDto {
    return EvaluationValueDto(
        id = id,
        available_value = available_value,
        default_value = default_value,
        object_address = object_address,
        evaluationObject_id = evaluationObject_id
    )
}

fun EvaluationSettings.toSerial(): EvaluationSettingsDto {
    return EvaluationSettingsDto(
        id = id,
        evaluation_repeat_period = evaluation_repeat_period,
        evaluation_repeat_times = evaluation_repeat_times,
        evaluation_begin_time = evaluation_begin_time,
        evaluation_last_time = evaluation_last_time,
        evaluation_end_time = evaluation_end_time,
        is_repetitive = is_repetitive,
        times_taken = times_taken,
        patient = patient,
        doctor = doctor,
        clinic = clinic,
        evaluation = evaluation
    )
}

fun EvaluationObject.toSerial(): EvaluationObjectDto {
    return EvaluationObjectDto(
        id = id,
        object_label = object_label,
        evaluation_screen = evaluation_screen,
        evaluation_order = evaluation_order,
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
        evaluation_name = evaluation_name,
        display_as_module = display_as_module,
        is_multilingual = is_multilingual,
        is_active = is_active,
        evaluation_settings = evaluation_settings.toSerial(),
        evaluation_objects = evaluation_objects.map { it.toSerial() }
    )
}

fun EvaluationDetailDto.toDomain(): EvaluationDetail {
    return when (this) {
        is EvaluationDetailStringDto -> EvaluationDetailString(
            dateTime = dateTime,
            evaluationObject = evaluationObject,
            value = value
        )
        is EvaluationDetailIntDto -> EvaluationDetailInt(
            dateTime = dateTime,
            evaluationObject = evaluationObject,
            value = value
        )
        is EvaluationDetailGenericDto -> EvaluationDetailGeneric(
            dateTime = dateTime,
            evaluationObject = evaluationObject,
            value = value
        )
    }
}

fun EvaluationDetail.toDto(): EvaluationDetailDto {
    return when (this) {
        is EvaluationDetailString -> EvaluationDetailStringDto(
            dateTime = dateTime,
            evaluationObject = evaluationObject,
            value = value
        )
        is EvaluationDetailInt -> EvaluationDetailIntDto(
            dateTime = dateTime,
            evaluationObject = evaluationObject,
            value = value
        )
        is EvaluationDetailGeneric -> EvaluationDetailGenericDto(
            dateTime = dateTime,
            evaluationObject = evaluationObject,
            value = value
        )
    }
}

fun EvaluationResultDto.toDomain(): EvaluationResult {
    return EvaluationResult(
        clinicId = clinicId,
        date = date,
        evaluation = evaluation,
        patientId = patient_id,
        results =  ArrayList(results.map { it.toDomain() as EvaluationDetailString })
    )
}

fun EvaluationResult.toDto(): EvaluationResultDto {
    return EvaluationResultDto(
        clinicId = clinicId,
        date = date,
        evaluation = evaluation,
        patient_id = patientId,
        results = ArrayList(results.map { it.toDto() as EvaluationDetailStringDto })
    )
}

