package maia.dmt.activities.data.activities

import io.ktor.client.HttpClient
import maia.dmt.activities.data.dto.ActivityItemDto
import maia.dmt.activities.data.mapper.toDomain
import maia.dmt.activities.data.mapper.toSerial
import maia.dmt.activities.domain.activities.ActivitiesService
import maia.dmt.activities.domain.model.ActivityItem
import maia.dmt.activities.domain.model.ActivityItemReport
import maia.dmt.core.data.networking.get
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map

class KtorActivitiesService(
    private val httpClient: HttpClient,
) : ActivitiesService {

    override suspend fun getActivities(clinicId: Int, patientId: Int): Result<List<ActivityItem>, DataError.Remote> {

        return httpClient.get<List<ActivityItemDto>>(
            route = "Activities_list/",
            queryParams = mapOf(
                "clinic_id" to clinicId,
                "patient_id" to patientId
            )
        ).map { it.map { it.toDomain() } }

    }

    override suspend fun reportActivity(result: ActivityItemReport): EmptyResult<DataError.Remote> {

        return httpClient.post(
            route = "Activity_report/",
            body = result.toSerial()
        )
    }

}