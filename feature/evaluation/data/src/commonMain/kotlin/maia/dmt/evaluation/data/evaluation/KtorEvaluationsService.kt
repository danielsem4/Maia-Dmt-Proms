package maia.dmt.evaluation.data.evaluation

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.core.data.dto.evaluation.EvaluationDto
import maia.dmt.core.data.mapper.toDomain
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.evaluation.EvaluationService

class KtorEvaluationsService(
    private val httpClient: HttpClient,
): EvaluationService {

    override suspend fun getEvaluations(
        clinicId: Int,
        patientId: Int
    ): Result<List<Evaluation>, DataError.Remote> {
        return httpClient.get<List<EvaluationDto>>(
            route = "PatientMeasurements/",
            queryParams = mapOf(
                "clinic_id" to clinicId,
                "patient_id" to patientId
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
        ).map { it.toDomain()}

    }

    override suspend fun uploadEvaluationResults(): Result<Unit, DataError.Remote> {
        TODO("Not yet implemented")
    }

}