package maia.dmt.proms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.home.data.notificaiton.FirebasePushNotificationService
import maia.dmt.home.domain.models.FcmTokenRequest
import maia.dmt.home.domain.notification.DeviceTokenService

class MainViewModel(
    private val sessionStorage: SessionStorage,
    private val pushNotificationService: FirebasePushNotificationService,
    private val deviceTokenService: DeviceTokenService
): ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private var previousDeviceToken: String? = null
    private var currentDeviceToken: String? = null

    init {
        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            _state.update { it.copy(
                isCheckingAuth = false,
                isLoggedIn = authInfo != null,
                user = authInfo?.user
            ) }

            observeUserAuth()
            observeDeviceToken()
        }
    }

    private fun observeUserAuth() {
        viewModelScope.launch {
            sessionStorage.observeAuthInfo().collect { authInfo ->
                val isLoggedIn = authInfo != null
                _state.update { it.copy(isLoggedIn = isLoggedIn) }

                if (isLoggedIn && currentDeviceToken != null) {
                    registerDeviceTokenIfNeeded(currentDeviceToken!!)
                }
                if (!isLoggedIn) {
                    previousDeviceToken = null
                }
            }
        }
    }

    private fun observeDeviceToken() {
        viewModelScope.launch {
            pushNotificationService.observeDeviceToken().collect { token ->
                currentDeviceToken = token

                if (_state.value.isLoggedIn && token != null) {
                    registerDeviceTokenIfNeeded(token)
                }
            }
        }
    }

    private fun registerDeviceTokenIfNeeded(token: String) {
        if (token != previousDeviceToken) {
            registerDeviceToken(token, getPlatform().name)
            previousDeviceToken = token
        }
    }

    private fun registerDeviceToken(token: String, platform: String) {
        viewModelScope.launch {
            deviceTokenService.registerDeviceToken(
                token = FcmTokenRequest(
                    user_id = state.value.user!!.id.toString(),
                    clinic_id = state.value.user!!.clinicId.toString(),
                    fcm_token = token
                )
            )
                .onSuccess {

                }
                .onFailure { error ->

                }
        }
    }
}