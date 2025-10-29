package maia.dmt.statistics.data.statistics


import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.statistics.data.dto.PatientEvaluationGraphsDto
import maia.dmt.statistics.data.mapper.toDomain
import maia.dmt.statistics.domain.model.PatientEvaluationGraphs
import maia.dmt.statistics.domain.statistics.StatisticsService

class KtorStatisticsService(
    private val httpClient: HttpClient
) : StatisticsService {

    override suspend fun getPatientEvaluationsGraphs(
        clinicId: Int,
        patientId: Int,
        evaluationsIds: ArrayList<String>
    ): Result<List<PatientEvaluationGraphs>, DataError.Remote> {
        val measurementsParam = evaluationsIds.joinToString(",", prefix = "[", postfix = "]")

        return httpClient.get<PatientEvaluationGraphsDto>(
            route = "getPatientEvaluationsGraphs/$clinicId/$patientId?measurements=$measurementsParam"
        ).map { listOf(it.toDomain()) }
    }


}