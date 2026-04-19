package maia.dmt.evaluation.data.measurements

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.evaluation.data.dto.MeasurementItemDto
import maia.dmt.evaluation.data.mapper.toDomain
import maia.dmt.evaluation.domain.measurements.MeasurementsService
import maia.dmt.evaluation.domain.model.MeasurementItem

class KtorMeasurementsService(
    private val httpClient: HttpClient,
) : MeasurementsService {

    override suspend fun getMeasurements(
        clinicId: String,
        userId: String
    ): Result<List<MeasurementItem>, DataError.Remote> {
        return httpClient.get<List<MeasurementItemDto>>(
            route = "api/v1/clinics/$clinicId/patients/$userId/measurements/",
            queryParams = mapOf(
                "type" to "QUESTIONNAIRES"
            )
        ).map { it.map { dto -> dto.toDomain() } }
    }
}
