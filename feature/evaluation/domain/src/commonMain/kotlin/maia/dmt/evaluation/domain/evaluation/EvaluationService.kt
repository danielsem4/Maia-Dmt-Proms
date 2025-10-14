package maia.dmt.evaluation.domain.evaluation

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.evaluation.domain.models.Evaluation

interface EvaluationService {
    suspend fun getEvaluations(clinicId: Int, patientId: Int): Result<List<Evaluation>, DataError.Remote>

}