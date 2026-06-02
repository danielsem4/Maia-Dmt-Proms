package maia.dmt.evaluation.data.evaluations

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.evaluation.data.dto.EvaluationItemDto
import maia.dmt.evaluation.data.mapper.toDomain
import maia.dmt.evaluation.domain.evaluations.EvaluationsService
import maia.dmt.evaluation.domain.model.EvaluationItem

class KtorEvaluationsService(
    private val httpClient: HttpClient,
) : EvaluationsService {

    override suspend fun getEvaluations(
        clinicId: String,
        userId: String
    ): Result<List<EvaluationItem>, DataError.Remote> {
        return httpClient.get<List<EvaluationItemDto>>(
            route = "api/v1/clinics/$clinicId/patients/$userId/evaluations/",
            queryParams = mapOf(
                "type" to "QUESTIONNAIRES"
            )
        ).map { it.map { dto -> dto.toDomain() } }
    }
}
