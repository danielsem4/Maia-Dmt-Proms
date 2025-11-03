package maia.dmt.statistics.domain.statistics

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.statistics.domain.model.PatientEvaluationGraphs

interface StatisticsService {

    suspend fun getPatientEvaluationsGraphs(
        clinicId: Int,
        patientId: Int,
        evaluationsIds: ArrayList<String>
    ): Result<List<PatientEvaluationGraphs>, DataError.Remote>


}