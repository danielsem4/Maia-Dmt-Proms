package maia.dmt.statistics.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PatientEvaluationGraphsDto(
    val patient: PatientEvaluationGraphAuthDto,
    val measurements_data: Map<String, MeasurementDataWrapperDto>,
    val medications: List<EvaluationGraphsMedicationDto>? = null,
    val show_medications: Boolean
)

@Serializable
data class PatientEvaluationGraphAuthDto(
    val id: Int,
    val name: String
)

@Serializable
data class MeasurementDataWrapperDto(
    val measurement: PatientEvaluationGraphsInfoDto,
    val data: Map<String, XYDataDto>
)

@Serializable
data class PatientEvaluationGraphsInfoDto(
    val id: Int,
    val name: String
)

@Serializable
data class XYDataDto(
    val x: List<String>,
    val y: List<String>
)

@Serializable
data class EvaluationGraphsMedicationDto(
    val id: String,
    val name: String,
    val form: String,
    val dosage: String,
    val time_taken: String
)

