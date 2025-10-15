package maia.dmt.core.domain.evaluation

import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result

interface EvaluationService {

    suspend fun getEvaluations(clinicId: Int, patientId: Int): Result<List<Evaluation>, DataError.Remote>

    suspend fun getEvaluation(clinicId: Int, patientId: Int, evaluationName: String): Result<Evaluation, DataError.Remote>

    suspend fun uploadEvaluationResults(): Result<Unit, DataError.Remote>

}