package maia.dmt.graphs.data.graphs

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.graphs.data.dto.ChartResponseDto
import maia.dmt.graphs.data.mapper.toDomain
import maia.dmt.graphs.domain.graphs.GraphsService
import maia.dmt.graphs.domain.models.ChartResponse

class KtorGraphsService(private val httpClient: HttpClient) : GraphsService {

    override suspend fun getGraphs(clinicId: Int, patientId: Int): Result<ChartResponse, DataError.Remote> {
        return httpClient.get<ChartResponseDto>(
            route = "getPatientEvaluationsGraphs/$clinicId/$patientId",
//            queryParams = mapOf(
//                "clinic_id" to clinicId,
//                "patient_id" to patientId
//            )
        ).map {  it.toDomain() }
    }
}

