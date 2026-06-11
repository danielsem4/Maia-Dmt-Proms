package maia.dmt.core.data.evaluation

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import kotlinx.serialization.json.JsonElement
import maia.dmt.core.data.dto.evaluation.EvaluationDto
import maia.dmt.core.data.dto.evaluation.EvaluationStructureDto
import maia.dmt.core.data.dto.evaluation.EvaluationSubmissionRequestDto
import maia.dmt.core.data.mapper.buildSubmissionDto
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.data.mapper.toDto
import maia.dmt.core.data.mapper.toSubmissionResult
import maia.dmt.core.data.networking.post
import maia.dmt.core.data.util.encodeValueToJson
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.dto.evaluation.EvaluationResult
import maia.dmt.core.domain.evaluation.EvaluationService
import maia.dmt.core.domain.evaluation.EvaluationStructure
import maia.dmt.core.domain.evaluation.EvaluationSubmissionResult

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
            route = "clinics/$clinicId/patients/$userId/evaluations/",
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

    override suspend fun getEvaluationStructure(
        clinicId: String,
        evaluationId: String
    ): Result<EvaluationStructure, DataError.Remote> {
        return httpClient.get<EvaluationStructureDto>(
            route = "api/v1/mobile/clinics/$clinicId/evaluations/$evaluationId/structure/"
        ).map { it.toDomain() }
    }

    override suspend fun uploadEvaluationResults(results: Any): Result<Unit, DataError.Remote> {
        return when (results) {
            is EvaluationResult -> {
                val dto = results.toDto()
                val jsonBody = try {
                    encodeValueToJson(dto)
                } catch (e: Exception) {
                    println("Error encoding to JSON: ${e.message}")
                    return Result.Failure(DataError.Remote.UNKNOWN)
                }

                httpClient.post(
                    route = "patientEvaluationResponse/",
                    body = jsonBody
                )
            }

            else -> {
                Result.Failure(DataError.Remote.UNKNOWN)
            }
        }
    }

    override suspend fun submitEvaluation(
        clinicId: String,
        evaluationId: String,
        structure: EvaluationStructure,
        answers: Map<String, String>,
        versionKey: String?
    ): Result<EvaluationSubmissionResult, DataError.Remote> {
        val requestDto = buildSubmissionDto(structure, answers, versionKey)
        return httpClient.post<EvaluationSubmissionRequestDto, JsonElement>(
            route = "api/v1/mobile/clinics/$clinicId/evaluations/$evaluationId/submit/",
            body = requestDto
        ).map { it.toSubmissionResult() }
    }
}
