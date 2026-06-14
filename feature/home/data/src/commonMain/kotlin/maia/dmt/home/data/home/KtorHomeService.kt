package maia.dmt.home.data.home

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.home.data.dto.HomeResponseDto
import maia.dmt.home.data.mapper.toDomain
import maia.dmt.home.domain.home.HomeService
import maia.dmt.home.domain.models.HomeData

class KtorHomeService(
    private val httpClient: HttpClient,
) : HomeService {
    override suspend fun getHomeData(
        clinicId: String,
        userId: String
    ): Result<HomeData, DataError.Remote> {
        return httpClient.get<HomeResponseDto>(
            route = "api/v1/mobile/clinics/$clinicId/patients/$userId/home/",
        ).map { response ->
            HomeData(
                modules = response.modules.filter { it.is_active }.map { it.toDomain() },
                evaluations = response.evaluations.map { it.toDomain() },
            )
        }
    }

    override suspend fun getDoctorHomeData(
        clinicId: String,
    ): Result<HomeData, DataError.Remote> {
        return httpClient.get<HomeResponseDto>(
            route = "api/v1/mobile/clinics/$clinicId/home/",
        ).map { response ->
            HomeData(
                modules = response.modules.filter { it.is_active }.map { it.toDomain() },
                evaluations = response.evaluations.map { it.toDomain() },
            )
        }
    }
}
