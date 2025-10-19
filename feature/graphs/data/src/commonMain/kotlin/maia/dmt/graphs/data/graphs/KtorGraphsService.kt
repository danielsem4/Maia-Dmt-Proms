package maia.dmt.graphs.data.graphs

import io.ktor.client.HttpClient
import maia.dmt.graphs.domain.graphs.GraphsService

class KtorGraphsService(private val httpClient: HttpClient) : GraphsService {
    override suspend fun getGraphs(clinicId: Int): Result<List<Any>> {
        TODO("Not yet implemented")
    }

}