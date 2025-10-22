package maia.dmt.home.domain.notification

import maia.dmt.core.domain.util.DataError
import maia.dmt.core.domain.util.EmptyResult

interface DeviceTokenService {

    suspend fun registerDeviceToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote>

    suspend fun unregisterDeviceToken(
        token: String,
    ): EmptyResult<DataError.Remote>

}