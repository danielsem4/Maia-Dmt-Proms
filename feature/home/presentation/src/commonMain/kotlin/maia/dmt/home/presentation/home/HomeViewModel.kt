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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
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

class HomeViewModel(
    private val homeService: HomeService,
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())

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
            HomeAction.OnLogoutClick -> logout()
            is HomeAction.OnFeatureClicked -> handleFeatureClick(action.moduleId)
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

            // Get auth info from session
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

            // Extract clinic_id from authInfo
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

    private fun mapModuleIcon(moduleId: Int): androidx.compose.ui.graphics.vector.ImageVector {
        // Map module IDs to appropriate icons
        return when (moduleId) {
            1 -> Icons.Default.Home
            2 -> Icons.Default.Person
            3 -> Icons.Default.Settings
            4 -> Icons.Default.Notifications
            5 -> Icons.Default.Search
            6 -> Icons.Default.Favorite
            7 -> Icons.Default.Email
            8 -> Icons.Default.ShoppingCart
            9 -> Icons.Default.AccountCircle
            else -> Icons.Default.Star
        }
    }

    private fun handleFeatureClick(moduleId: Int) {
        println("Feature clicked: $moduleId")
    }

    private fun logout() {
        viewModelScope.launch {
            sessionStorage.set(null)
        }
    }
}