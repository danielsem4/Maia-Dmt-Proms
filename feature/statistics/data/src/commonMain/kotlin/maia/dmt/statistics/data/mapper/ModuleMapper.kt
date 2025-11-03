package maia.dmt.statistics.data.mapper

import maia.dmt.statistics.data.dto.EvaluationGraphsMedicationDto
import maia.dmt.statistics.data.dto.MeasurementDataWrapperDto
import maia.dmt.statistics.data.dto.PatientEvaluationGraphAuthDto
import maia.dmt.statistics.data.dto.PatientEvaluationGraphsDto
import maia.dmt.statistics.data.dto.PatientEvaluationGraphsInfoDto
import maia.dmt.statistics.data.dto.XYDataDto
import maia.dmt.statistics.domain.model.EvaluationGraphsMedication
import maia.dmt.statistics.domain.model.MeasurementDataWrapper
import maia.dmt.statistics.domain.model.PatientEvaluationGraphAuth
import maia.dmt.statistics.domain.model.PatientEvaluationGraphs
import maia.dmt.statistics.domain.model.PatientEvaluationGraphsInfo
import maia.dmt.statistics.domain.model.XYData


fun PatientEvaluationGraphsDto.toDomain(): PatientEvaluationGraphs =
    PatientEvaluationGraphs(
        patient = patient.toDomain(),
        measurements_data = measurements_data.mapValues { it.value.toDomain() },
        medications = medications?.map { it.toDomain() },
        show_medications = show_medications
    )

fun PatientEvaluationGraphAuthDto.toDomain(): PatientEvaluationGraphAuth =
    PatientEvaluationGraphAuth(
        id = id,
        name = name
    )

fun MeasurementDataWrapperDto.toDomain(): MeasurementDataWrapper =
    MeasurementDataWrapper(
        measurement = measurement.toDomain(),
        data = data.mapValues { it.value.toDomain() }
    )

fun PatientEvaluationGraphsInfoDto.toDomain(): PatientEvaluationGraphsInfo =
    PatientEvaluationGraphsInfo(
        id = id,
        name = name
    )

fun XYDataDto.toDomain(): XYData =
    XYData(
        x = x,
        y = y
    )

fun EvaluationGraphsMedicationDto.toDomain(): EvaluationGraphsMedication =
    EvaluationGraphsMedication(
        id = id,
        name = name,
        form = form,
        dosage = dosage,
        time_taken = time_taken
    )
