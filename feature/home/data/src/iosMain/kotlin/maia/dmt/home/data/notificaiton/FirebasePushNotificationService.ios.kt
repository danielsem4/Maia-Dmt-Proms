package maia.dmt.home.data.notificaiton

import kotlinx.coroutines.flow.Flow
import maia.dmt.home.domain.notification.PushNotificationService

actual class FirebasePushNotificationService :PushNotificationService {
    actual override fun observeDeviceToken(): Flow<String?> {
        TODO("Not yet implemented")
    }
}