package maia.dmt.home.domain.notification

import kotlinx.coroutines.flow.Flow

interface PushNotificationService {

    fun observeDeviceToken(): Flow<String?>

}