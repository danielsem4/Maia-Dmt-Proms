package maia.dmt.home.data.notificaiton

import io.ktor.client.HttpClient
import maia.dmt.core.data.networking.delete
import maia.dmt.core.data.networking.post
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.home.data.dto.FcmTokenRequestDto
import maia.dmt.home.domain.notification.DeviceTokenService

class KtorDeviceTokenService(
    private val httpClient: HttpClient
): DeviceTokenService {

    override suspend fun registerDeviceToken(
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "api/v1/mobile/fcm-token/",
            body = FcmTokenRequestDto(token = token)
        )
    }

    override suspend fun unregisterDeviceToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "api/v1/mobile/fcm-token/",
            body = FcmTokenRequestDto(token = token)
        )
    }
}
