package maia.dmt.activities.domain.activities

import maia.dmt.activities.domain.model.ActivityItem
import maia.dmt.activities.domain.model.ActivityItemReport
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result

interface ActivitiesService {

    suspend fun getActivities(clinicId: Int): Result<List<ActivityItem>, DataError.Remote>

    suspend fun reportActivity(result: ActivityItemReport): EmptyResult<DataError.Remote>




}