package maia.dmt.home.data.home

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.firstOrNull
import maia.dmt.core.data.networking.get
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.Result
import maia.dmt.core.domain.util.map
import maia.dmt.home.data.dto.ModuleDto
import maia.dmt.home.data.mapper.toDomain
import maia.dmt.home.domain.home.HomeService
import maia.dmt.home.domain.models.Module

class KtorHomeService(
    private val httpClient: HttpClient,
) : HomeService {
    override suspend fun getModules(clinicId: Int): Result<List<Module>, DataError.Remote> {
        return httpClient.get<List<ModuleDto>>(
            route = "getModules/",
            queryParams = mapOf(
                "clinic_id" to clinicId
            )
        ).map { it.map { it.toDomain() } }
    }

    override suspend fun logout(): EmptyResult<DataError.Remote> {

        return httpClient.post(
            route = "logout/",
            body = Unit
        )
    }
}
