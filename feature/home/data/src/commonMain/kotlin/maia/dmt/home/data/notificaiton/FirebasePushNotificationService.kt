package maia.dmt.home.data.notificaiton

import kotlinx.coroutines.flow.Flow
import maia.dmt.home.domain.notification.PushNotificationService

expect class FirebasePushNotificationService: PushNotificationService {
    override fun observeDeviceToken(): Flow<String?>
}