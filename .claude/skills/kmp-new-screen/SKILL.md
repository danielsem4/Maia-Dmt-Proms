---
name: kmp-new-screen
description: Adding a new screen (composable + ViewModel + State/Action/Event) to an existing feature in the DMTProms KMP codebase
---

# Adding a New Screen to an Existing Feature

Use this skill when adding a screen, page, or UI flow within an existing feature module.

## The 5-File Pattern

Every screen needs these 5 files in a directory named after the screen:

```
feature/<feature>/presentation/src/commonMain/kotlin/maia/dmt/<feature>/presentation/<screenName>/
  <ScreenName>Screen.kt    # Root + Screen composables
  <ScreenName>ViewModel.kt # ViewModel with state management
  <ScreenName>State.kt     # Data class holding UI state
  <ScreenName>Action.kt    # Sealed interface of user actions
  <ScreenName>Event.kt     # Sealed interface of one-time events
```

## File 1: State.kt

```kotlin
package maia.dmt.<feature>.presentation.<screenName>

import maia.dmt.core.presentation.util.UiText

data class <ScreenName>State(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    // Add screen-specific state fields with sensible defaults
)
```

## File 2: Action.kt

```kotlin
package maia.dmt.<feature>.presentation.<screenName>

sealed interface <ScreenName>Action {
    data object OnBackClick : <ScreenName>Action
    data object OnRefresh : <ScreenName>Action
    // data class for actions with parameters:
    // data class OnItemClicked(val itemId: String) : <ScreenName>Action
    // data object for parameterless actions:
    // data object OnSubmit : <ScreenName>Action
}
```

## File 3: Event.kt

Events are one-time side effects (navigation, toasts). They flow through a Channel, not StateFlow.

```kotlin
package maia.dmt.<feature>.presentation.<screenName>

sealed interface <ScreenName>Event {
    data object NavigateBack : <ScreenName>Event
    // data class Error(val message: UiText) : <ScreenName>Event
    // data object SubmitSuccess : <ScreenName>Event
}
```

## File 4: ViewModel.kt

```kotlin
package maia.dmt.<feature>.presentation.<screenName>

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.core.domain.auth.SessionStorage
import maia.dmt.core.domain.util.onFailure
import maia.dmt.core.domain.util.onSuccess
import maia.dmt.core.presentation.util.toUiText

class <ScreenName>ViewModel(
    private val service: <Feature>Service,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(<ScreenName>State())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<<ScreenName>Event>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    fun onAction(action: <ScreenName>Action) {
        when (action) {
            <ScreenName>Action.OnBackClick -> {
                viewModelScope.launch { eventChannel.send(<ScreenName>Event.NavigateBack) }
            }
            <ScreenName>Action.OnRefresh -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Get session info for API calls
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            val clinicId = authInfo?.user?.clinics?.firstOrNull() ?: return@launch

            service.getItems(clinicId, authInfo.user?.id ?: "")
                .onSuccess { items ->
                    _state.update { it.copy(isLoading = false, items = items) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }
}
```

### For CogTest screens requiring inactivity timeout:

```kotlin
class <ScreenName>ViewModel(
    // ... dependencies
) : InactivityViewModel<<ScreenName>State>(
    initialState = <ScreenName>State(),
    inactivityTimeoutMs = 120_000L // 2 minutes
) {
    val state = _state.asStateFlow()
    // ... Channel, events same as above

    override fun showInactivityDialog() {
        _state.update { it.copy(showInactivityDialog = true) }
    }

    override fun dismissInactivityDialog() {
        _state.update { it.copy(showInactivityDialog = false) }
    }

    fun onAction(action: <ScreenName>Action) {
        resetInactivityTimer() // Reset on every user action
        // ... handle actions
    }
}
```

## File 5: Screen.kt

The Root/Screen split pattern: `Root` wires ViewModel, `Screen` is a pure composable.

```kotlin
package maia.dmt.<feature>.presentation.<screenName>

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun <ScreenName>Root(
    viewModel: <ScreenName>ViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is <ScreenName>Event.NavigateBack -> onNavigateBack()
        }
    }

    <ScreenName>Screen(state = state, onAction = viewModel::onAction)
}

@Composable
fun <ScreenName>Screen(
    state: <ScreenName>State,
    onAction: (<ScreenName>Action) -> Unit,
) {
    val configuration = currentDeviceConfiguration()

    DmtBaseScreen(
        titleText = "Screen Title",  // Use stringResource(Res.string.xxx) for localized
        onIconClick = { onAction(<ScreenName>Action.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    // Screen content here
                }
            }
        }
    )
}
```

## Step 6: Register ViewModel in DI

Add to the feature's presentation DI module at `feature/<feature>/presentation/src/commonMain/kotlin/maia/dmt/<feature>/presentation/di/<Feature>PresentationModule.kt`:

```kotlin
viewModelOf(::<ScreenName>ViewModel)
```

## Step 7: Add Navigation Route

Add to `feature/<feature>/presentation/src/commonMain/kotlin/maia/dmt/<feature>/presentation/navigation/<Feature>GraphRoutes.kt`:

```kotlin
@Serializable
data object <ScreenName> : <Feature>GraphRoutes

// Or with parameters:
@Serializable
data class <ScreenName>(val itemId: String) : <Feature>GraphRoutes
```

## Step 8: Register in Graph Builder

Add to `feature/<feature>/presentation/src/commonMain/kotlin/maia/dmt/<feature>/presentation/navigation/<Feature>Graph.kt`:

```kotlin
composable<<Feature>GraphRoutes.<ScreenName>> {
    <ScreenName>Root(
        onNavigateBack = { navController.popBackStack() }
    )
}

// With parameters:
composable<<Feature>GraphRoutes.<ScreenName>> { backStackEntry ->
    <ScreenName>Root(
        onNavigateBack = { navController.popBackStack() }
    )
}
```

## Key Patterns

- **Root composable**: Gets ViewModel via `koinViewModel()`, collects state via `collectAsStateWithLifecycle()`, observes events via `ObserveAsEvents`
- **Screen composable**: Pure function, receives `state` and `onAction` lambda only, no ViewModel reference
- **Navigation events**: Always flow through `Channel` -> `events` flow, never called directly in Screen
- **State updates**: Always use `_state.update { it.copy(...) }` pattern
- **Responsive design**: Use `currentDeviceConfiguration()` to get `DeviceConfiguration` enum, branch sizing by device type
- **Layout wrapper**: Use `DmtBaseScreen` for standard top-bar + content layout
- **Error display**: Convert `DataError` to `UiText` via `error.toUiText()` extension
- **Loading state**: Use `CircularProgressIndicator()` while loading
- **Feature components**: Place in `<feature>/presentation/components/` directory
- **Design system components**: Import from `maia.dmt.core.designsystem.components.*` (see `kmp-ui-components` skill)
