package maia.dmt.home.data.home

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.home.data.dto.HomeResponseDto
import maia.dmt.home.data.mapper.toDomain
import maia.dmt.home.domain.home.HomeService
import maia.dmt.home.domain.models.Module

class KtorHomeService(
    private val httpClient: HttpClient,
) : HomeService {
    override suspend fun getModules(clinicId: String): Result<List<Module>, DataError.Remote> {
        return httpClient.get<HomeResponseDto>(
            route = "mobile/clinics/$clinicId/home/",
        ).map { response ->
            val modules = response.modules.filter { it.is_active }.map { it.toDomain() }
            val measurements = response.measurements.filter { it.is_active }.map { it.toDomain() }
            modules + measurements
        }
    }
}
