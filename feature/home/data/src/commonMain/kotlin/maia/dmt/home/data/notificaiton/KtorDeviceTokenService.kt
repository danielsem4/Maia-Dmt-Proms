package maia.dmt.home.data.notificaiton

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.get
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.core.domain.util.map
import maia.dmt.home.data.dto.FcmTokenRequestDto
import maia.dmt.home.data.dto.ModuleDto
import maia.dmt.home.data.mapper.toSerial
import maia.dmt.home.domain.models.FcmTokenRequest
import maia.dmt.home.domain.notification.DeviceTokenService

class KtorDeviceTokenService(
    private val httpClient: HttpClient
): DeviceTokenService {

    override suspend fun registerDeviceToken(
        token: FcmTokenRequest
    ): EmptyResult<DataError.Remote> {

        return httpClient.post(
            route = "registerDeviceToken/",
            body = token.toSerial()
        )
    }

    override suspend fun unregisterDeviceToken(token: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }


}