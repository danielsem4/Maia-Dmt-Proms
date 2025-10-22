package maia.dmt.home.data.notificaiton

import io.ktor.client.HttpClient
import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.home.domain.notification.DeviceTokenService

class KtorDeviceTokenService(
    private val httpClient: HttpClient
): DeviceTokenService {

    override suspend fun registerDeviceToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
        // route get_fcm_token/
        // data class:
        //  data class FcmTokenRequest(
        //    val user_id: String,
        //    val clinic_id: String,
        //    val fcm_token: String
        //)
    }

    override suspend fun unregisterDeviceToken(token: String): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }


}