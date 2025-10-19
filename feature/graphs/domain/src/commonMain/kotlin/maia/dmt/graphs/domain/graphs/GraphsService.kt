package maia.dmt.graphs.domain.graphs

interface GraphsService {

    suspend fun getGraphs(clinicId: Int): Result<List<Any>>

}