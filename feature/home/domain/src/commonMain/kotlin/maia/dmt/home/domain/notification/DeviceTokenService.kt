package maia.dmt.home.domain.notification

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult
import maia.dmt.home.domain.models.FcmTokenRequest

interface DeviceTokenService {

    suspend fun registerDeviceToken(
        token: FcmTokenRequest
    ): EmptyResult<DataError.Remote>

    suspend fun unregisterDeviceToken(
        token: String,
    ): EmptyResult<DataError.Remote>

}