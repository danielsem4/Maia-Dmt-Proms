package maia.dmt.evaluation.domain.evaluations

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.evaluation.domain.model.EvaluationItem

interface EvaluationsService {
    suspend fun getEvaluations(clinicId: String, userId: String): Result<List<EvaluationItem>, DataError.Remote>
}
