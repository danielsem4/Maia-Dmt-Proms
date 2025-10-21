package maia.dmt.core.domain.evaluation

import kotlinx.serialization.json.Json
import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result

interface EvaluationService {

    suspend fun getEvaluations(clinicId: Int, patientId: Int): Result<List<Evaluation>, DataError.Remote>

    suspend fun getEvaluation(clinicId: Int, patientId: Int, evaluationName: String): Result<Evaluation, DataError.Remote>

    suspend fun uploadEvaluationResults(results: Any): Result<Unit, DataError.Remote>

}