package maia.dmt.evaluation.data.evaluations

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.evaluation.data.dto.EvaluationDto
import maia.dmt.evaluation.data.mapper.toDomain
import maia.dmt.evaluation.domain.evaluations.EvaluationService
import maia.dmt.evaluation.domain.models.Evaluation

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

}