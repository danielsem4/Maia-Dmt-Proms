package maia.dmt.core.data.evaluation

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.core.data.dto.evaluation.EvaluationDto
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.data.mapper.toDto
import maia.dmt.core.data.networking.post
import maia.dmt.core.data.util.encodeValueToJson
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.dto.evaluation.MeasurementResult
import maia.dmt.core.domain.evaluation.EvaluationService

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
        clinicId: Int,
        patientId: Int,
        all: Boolean
    ): Result<List<Evaluation>, DataError.Remote> {
        return httpClient.get<List<EvaluationDto>>(
            route = "PatientMeasurements/",
            queryParams = mapOf(
                "clinic_id" to clinicId,
                "patient_id" to patientId,
                "all" to all
            )
        ).map { it.map { it.toDomain() } }
    }

    override suspend fun getEvaluation(
        clinicId: Int,
        patientId: Int,
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
}
