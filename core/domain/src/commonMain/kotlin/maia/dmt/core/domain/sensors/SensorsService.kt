package maia.dmt.core.domain.sensors

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result

interface SensorsService {

    suspend fun uploadSensorsAggResults(request: Any): Result<Unit, DataError.Remote>

}