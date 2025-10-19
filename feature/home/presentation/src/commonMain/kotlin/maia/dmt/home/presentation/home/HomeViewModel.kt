package maia.dmt.home.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.activities_icon
import dmtproms.feature.home.presentation.generated.resources.clock_icon
import dmtproms.feature.home.presentation.generated.resources.evaluation_icon
import dmtproms.feature.home.presentation.generated.resources.file_upload_icon
import dmtproms.feature.home.presentation.generated.resources.hitber_icon
import dmtproms.feature.home.presentation.generated.resources.logout_icon
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
import org.jetbrains.compose.resources.imageResource

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
            is HomeAction.OnFeatureClicked -> handleFeatureClick(action.moduleId)
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
                            icon = mapModuleIcon(module.module_id),
                            text = module.module_name,
                            onClick = { onAction(HomeAction.OnFeatureClicked(module.module_id)) }
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

    private fun mapModuleIcon(moduleId: Int): DrawableResource {
        // Map module IDs to appropriate icons
        return when (moduleId) {
            3 -> (Res.drawable.file_upload_icon)
            4 -> Res.drawable.evaluation_icon
            7 -> Res.drawable.medications_icon
            8 -> Res.drawable.activities_icon
            13 ->Res.drawable.memory_icon
            16 ->Res.drawable.clock_icon
            20 ->Res.drawable.orientation_icon
            22 ->Res.drawable.hitber_icon
            28 -> Res.drawable.statistics_icon
            9 -> Res.drawable.hitber_icon
            else -> Res.drawable.logout_icon
        }
    }

    private fun handleFeatureClick(moduleId: Int) {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.ModuleClicked(moduleId))
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update {
                it.copy(showLogoutDialog = false)
            }
            sessionStorage.set(null)
            eventChannel.send(HomeEvent.LogoutSuccess)
        }
    }
}