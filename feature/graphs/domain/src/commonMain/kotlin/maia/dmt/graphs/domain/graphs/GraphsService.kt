package maia.dmt.graphs.domain.graphs

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.graphs.domain.models.ChartResponse

interface GraphsService {

    suspend fun getGraphs(clinicId: Int, patientId: Int): Result<ChartResponse, DataError.Remote>

}