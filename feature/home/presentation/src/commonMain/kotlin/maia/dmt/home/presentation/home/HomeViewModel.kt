package maia.dmt.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.AuthService
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.sensors.SensorController
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.home.domain.home.HomeService
import maia.dmt.home.domain.notification.DeviceTokenService
import maia.dmt.home.domain.notification.PushNotificationService
import maia.dmt.home.presentation.mapper.mapModuleIcon
import maia.dmt.home.presentation.mapper.mapModuleNameToUiText
import maia.dmt.home.presentation.module.ModuleUiModel

class HomeViewModel(
    private val homeService: HomeService,
    private val sessionStorage: SessionStorage,
    private val authService: AuthService,
    private val pushNotificationService: PushNotificationService,
    private val deviceTokenService: DeviceTokenService,
    private val sensorController: SensorController
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()
    val state = _state.asStateFlow()

    private var hasLoadedInitialData = false
    private var currentFcmToken: String? = null

    init {
        if (!hasLoadedInitialData) {
            setPatient()
            loadModules()
            observeFcmToken()
            hasLoadedInitialData = true
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> loadModules()

            HomeAction.OnLogoutClick -> _state.update { it.copy(showLogoutDialog = true) }
            HomeAction.OnLogoutCancel -> _state.update { it.copy(showLogoutDialog = false) }
            HomeAction.OnLogoutConfirm -> logout()

            is HomeAction.OnFeatureClicked -> handleFeatureClick(action.moduleName)
            is HomeAction.OnEvaluationClicked -> handleEvaluationClick(action.evaluationId)
            HomeAction.OnShowParkinsonDialog -> _state.update { it.copy(showParkinsonDialog = true) }
            HomeAction.OnParkinsonDialogDismiss -> _state.update { it.copy(showParkinsonDialog = false) }

            HomeAction.OnDismissSensorDialog -> _state.update { it.copy(showSensorPermissionDialog = false) }
            HomeAction.OnSensorPermissionGranted -> {
                startSensors()
            }
        }
    }

    private fun loadModules() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingModules = true, modulesError = null) }

            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()

            if (authInfo == null) {
                _state.update {
                    it.copy(
                        isLoadingModules = false,
                        modulesError = UiText.DynamicString("Session not found. Please login again.")
                    )
                }
                return@launch
            }

            val clinicId = sessionStorage.getActiveClinicId()
            val userId = authInfo.user?.id

            if (clinicId.isNullOrEmpty() || userId == null) {
                _state.update { it.copy(isLoadingModules = false, modulesError = UiText.DynamicString("Invalid Session")) }
                return@launch
            }

            homeService.getHomeData(clinicId, userId)
                .onSuccess { homeData ->
                    val moduleUiModels = homeData.modules.map { module ->
                        ModuleUiModel(
                            icon = mapModuleIcon(module.name),
                            text = mapModuleNameToUiText(module.name),
                            onClick = {
                                if (module.name == "Parkinson report") onAction(HomeAction.OnShowParkinsonDialog)
                                else onAction(HomeAction.OnFeatureClicked(module.name))
                            }
                        )
                    }

                    val evaluationUiModels = homeData.evaluations.map { evaluation ->
                        ModuleUiModel(
                            icon = mapModuleIcon(evaluation.name),
                            text = mapModuleNameToUiText(evaluation.name),
                            onClick = {
                                onAction(HomeAction.OnEvaluationClicked(evaluation.evaluationId))
                            }
                        )
                    }

                    val allUiModels = moduleUiModels + evaluationUiModels

                    val hasSensorModule = homeData.modules.any {
                        it.name.contains("Sensor", ignoreCase = true) ||
                                it.name.contains("Tremor", ignoreCase = true)
                    }

                    val showPermissionDialog = hasSensorModule &&
                            !sensorController.hasSensorPermission() &&
                            !_state.value.isSensorsActive

                    _state.update {
                        it.copy(
                            modules = allUiModels,
                            isLoadingModules = false,
                            showSensorPermissionDialog = showPermissionDialog
                        )
                    }

                    if (hasSensorModule && sensorController.hasSensorPermission() && !_state.value.isSensorsActive) {
                        startSensors()
                    }

                    if (homeData.modules.any { it.name == "Parkinson report" } && !_state.value.hasShownParkinsonOnLaunch) {
                        _state.update { it.copy(showParkinsonDialog = true, hasShownParkinsonOnLaunch = true) }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoadingModules = false, modulesError = error.toUiText()) }
                }
        }
    }

    private fun startSensors() {
        sensorController.startSensorService()
        _state.update { it.copy(showSensorPermissionDialog = false, isSensorsActive = true) }
    }

    private fun setPatient() {
        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            _state.update { it.copy(patient = authInfo?.user) }
        }
    }

    private fun handleFeatureClick(name: String) {
        viewModelScope.launch { eventChannel.send(HomeEvent.ModuleClicked(name)) }
    }

    private fun handleEvaluationClick(evaluationId: String) {
        viewModelScope.launch { eventChannel.send(HomeEvent.EvaluationClicked(evaluationId)) }
    }

    private fun observeFcmToken() {
        viewModelScope.launch {
            pushNotificationService.observeDeviceToken().collect { token -> currentFcmToken = token }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingOut = true) }

            currentFcmToken?.let { token ->
                deviceTokenService.unregisterDeviceToken(token)
            }

            authService.logout().onSuccess {
                _state.update { it.copy(showLogoutDialog = false, isLoggingOut = false) }
                sessionStorage.set(null)
                eventChannel.send(HomeEvent.LogoutSuccess)
            }.onFailure {
                _state.update { it.copy(showLogoutDialog = true, isLoggingOut = false) }
            }
        }
    }
}