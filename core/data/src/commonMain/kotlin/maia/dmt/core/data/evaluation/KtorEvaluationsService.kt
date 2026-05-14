package maia.dmt.core.data.evaluation

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.core.data.dto.evaluation.EvaluationDto
import maia.dmt.core.data.dto.measurement.MeasurementStructureDto
import maia.dmt.core.data.mapper.buildSubmissionRequest
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.data.mapper.toDto
import maia.dmt.core.data.networking.post
import maia.dmt.core.data.util.encodeValueToJson
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.dto.evaluation.MeasurementResult
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.measurement.MeasurementStructure

class KtorEvaluationsService(
    private val httpClient: HttpClient,
) : EvaluationService {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
        encodeDefaults = true
    }

    override suspend fun getEvaluations(
        clinicId: String,
        userId: String,
    ): Result<List<Evaluation>, DataError.Remote> {
        return httpClient.get<List<EvaluationDto>>(
            route = "clinics/$clinicId/patients/$userId/measurements/",
            queryParams = mapOf(
                "type" to "QUESTIONNAIRES"
            )
        ).map { it.map { it.toDomain() } }
    }

    override suspend fun getEvaluation(
        clinicId: String,
        patientId: String,
        evaluationName: String
    ): Result<Evaluation, DataError.Remote> {

        return httpClient.get<EvaluationDto>(
            route = "getOneEvaluation/",
            queryParams = mapOf(
                "clinic_id" to clinicId,
                "patient_id" to patientId,
                "Evaluation_name" to evaluationName
            )
        ).map { it.toDomain() }

    }

    override suspend fun getMeasurementStructure(
        clinicId: String,
        measurementId: String
    ): Result<MeasurementStructure, DataError.Remote> {
        return httpClient.get<MeasurementStructureDto>(
            route = "api/v1/mobile/clinics/$clinicId/measurements/$measurementId/structure/"
        ).map { it.toDomain() }
    }

    override suspend fun uploadEvaluationResults(results: Any): Result<Unit, DataError.Remote> {
        return when (results) {
            is MeasurementResult -> {
                val dto = results.toDto()
                val jsonBody = try {
                    encodeValueToJson(dto)
                } catch (e: Exception) {
                    println("Error encoding to JSON: ${e.message}")
                    return Result.Failure(DataError.Remote.UNKNOWN)
                }

                httpClient.post(
                    route = "patientMeasureResponse/",
                    body = jsonBody
                )
            }

            else -> {
                Result.Failure(DataError.Remote.UNKNOWN)
            }
        }
    }

    override suspend fun submitMeasurement(
        clinicId: String,
        structure: MeasurementStructure,
        answers: Map<String, String>
    ): Result<Unit, DataError.Remote> {
        val requestDto = buildSubmissionRequest(structure, answers)
        return httpClient.post(
            route = "api/v1/mobile/clinics/$clinicId/measurements/${structure.measurementId}/submit/",
            body = requestDto
        )
    }
}
