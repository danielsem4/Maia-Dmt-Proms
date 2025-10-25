package maia.dmt.home.data.notificaiton

import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import maia.dmt.core.domain.logger.DmtLogger
import maia.dmt.home.domain.notification.PushNotificationService
import kotlin.coroutines.coroutineContext


actual class FirebasePushNotificationService(
    private val logger: DmtLogger
) : PushNotificationService {

    actual override fun observeDeviceToken(): Flow<String?> = flow {
        try {
            val fcmToken = Firebase.messaging.token.await()
            logger.info("Initial FCM token received: $fcmToken")
            emit(fcmToken)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            logger.error("Failed to get FCM token", e)
            emit(null)
        }
    }
}