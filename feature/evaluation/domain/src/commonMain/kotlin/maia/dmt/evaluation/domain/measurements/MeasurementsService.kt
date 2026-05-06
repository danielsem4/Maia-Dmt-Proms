package maia.dmt.evaluation.domain.measurements

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.evaluation.domain.model.MeasurementItem

interface MeasurementsService {
    suspend fun getMeasurements(clinicId: String, userId: String): Result<List<MeasurementItem>, DataError.Remote>
}
