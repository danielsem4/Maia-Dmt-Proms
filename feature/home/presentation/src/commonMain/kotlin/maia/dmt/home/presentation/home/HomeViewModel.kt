package maia.dmt.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.activities_icon
import dmtproms.feature.home.presentation.generated.resources.clock_icon
import dmtproms.feature.home.presentation.generated.resources.evaluation_icon
import dmtproms.feature.home.presentation.generated.resources.file_upload_icon
import dmtproms.feature.home.presentation.generated.resources.hitber_icon
import dmtproms.feature.home.presentation.generated.resources.medications_icon
import dmtproms.feature.home.presentation.generated.resources.memory_icon
import dmtproms.feature.home.presentation.generated.resources.orientation_icon
import dmtproms.feature.home.presentation.generated.resources.statistics_icon
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.UiText
import maia.dmt.core.presentation.util.toUiText
import maia.dmt.home.domain.home.HomeService
import maia.dmt.home.presentation.module.ModuleUiModel
import org.jetbrains.compose.resources.DrawableResource

class HomeViewModel(
    private val homeService: HomeService,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadModules()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnLogoutClick -> showLogoutDialog()
            HomeAction.OnLogoutConfirm -> logout()
            HomeAction.OnLogoutCancel -> dismissLogoutDialog()
            is HomeAction.OnFeatureClicked -> handleFeatureClick(action.moduleName)
        }
    }

    private fun showLogoutDialog() {
        _state.update {
            it.copy(showLogoutDialog = true)
        }
    }

    private fun dismissLogoutDialog() {
        _state.update {
            it.copy(showLogoutDialog = false)
        }
    }

    private fun loadModules() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoadingModules = true,
                    modulesError = null
                )
            }

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

            val clinicId = authInfo.user?.clinicId

            if (clinicId == null || clinicId == 0) {
                _state.update {
                    it.copy(
                        isLoadingModules = false,
                        modulesError = UiText.DynamicString("No clinic ID found in session.")
                    )
                }
                return@launch
            }

            homeService.getModules(clinicId)
                .onSuccess { modules ->
                    val moduleUiModels = modules.map { module ->
                        ModuleUiModel(
                            icon = mapModuleIcon(module.module_name),
                            text = module.module_name,
                            onClick = { onAction(HomeAction.OnFeatureClicked(module.module_name)) }
                        )
                    }

                    _state.update {
                        it.copy(
                            modules = moduleUiModels,
                            isLoadingModules = false,
                            modulesError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingModules = false,
                            modulesError = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun mapModuleIcon(moduleName: String): DrawableResource {

        return when (moduleName.lowercase()) {
            "document share" -> (Res.drawable.file_upload_icon)
            "measurements" -> Res.drawable.evaluation_icon
            "medications" -> Res.drawable.medications_icon
            "activities" -> Res.drawable.activities_icon
            "memory" ->Res.drawable.memory_icon
            "cdt" ->Res.drawable.clock_icon
            "orientation" ->Res.drawable.orientation_icon
            "hitber" ->Res.drawable.hitber_icon
            "statistics" -> Res.drawable.statistics_icon
            else -> Res.drawable.hitber_icon
        }
    }

    private fun handleFeatureClick(moduleName: String) {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.ModuleClicked(moduleName))
        }
    }

    private fun logout() {
        viewModelScope.launch {

        }
            viewModelScope.launch {
                _state.update {
                    it.copy(isLoggingOut = true)
                }
            homeService.logout()
                .onSuccess {
                    _state.update {
                        it.copy(
                            showLogoutDialog = false,
                            isLoggingOut = false
                        )
                    }
                    sessionStorage.set(null)
                    eventChannel.send(HomeEvent.LogoutSuccess)
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            showLogoutDialog = true,
                            isLoggingOut = false
                        )
                    }

                }
        }
    }
}