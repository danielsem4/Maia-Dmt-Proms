package maia.dmt.home.data.notificaiton

import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.home.domain.notification.DeviceTokenService
import org.koin.android.ext.android.inject

class DmtFirebaseNotificationService: FirebaseMessagingService() {

    private val deviceTokenService by inject<DeviceTokenService>()
    private val sessionStorage by inject<SessionStorage>()
    private val applicationScope by inject<CoroutineScope>()


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        applicationScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            if (authInfo != null) {
                deviceTokenService.registerDeviceToken(
                    token = token,
                    platform = "ANDROID"
                )
            }
        }

    }

}