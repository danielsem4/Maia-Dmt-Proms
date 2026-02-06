package maia.dmt.pass.presentation.passContacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contacts_page_first_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contacts_page_second_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contacts_page_thired_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_person_names
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_search_at_latter_h_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_search_for_hana_choen_in_contacts_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_witch_contact_are_we_looking_for_pass
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.inactivity.InactivityHandler
import maia.dmt.pass.presentation.components.PassContactItem
import maia.dmt.pass.presentation.components.PassMediationDialog
import maia.dmt.pass.presentation.model.MediationConfig
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassContactsRoot(
    onNavigateToNext: () -> Unit,
    viewModel: PassContactsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val timeoutDuration = if (state.errorCount == 0) 25_000L else 15_000L

    val inactivityHandler = remember(scope, timeoutDuration) {
        InactivityHandler(
            scope = scope,
            timeoutMs = timeoutDuration,
            onInactivityTimeout = { _ ->
                viewModel.onAction(PassContactsAction.OnTimeout)
            }
        )
    }

    DisposableEffect(state.showTimeoutDialog, timeoutDuration) {
        if (!state.showTimeoutDialog) {
            inactivityHandler.start()
        }
        onDispose {
            inactivityHandler.cancel()
        }
    }

    var touchCounter by remember { mutableStateOf(0) }
    LaunchedEffect(touchCounter) {
        if (!state.showTimeoutDialog) {
            inactivityHandler.reset()
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event) {
                PassContactsEvent.NavigateToNextScreen -> onNavigateToNext()
                PassContactsEvent.NavigateToSuccess -> onNavigateToNext()
            }
        }
    }

    val mediations = remember {
        listOf(
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_contacts_page_first_assist,
                audioUrlRes = Res.string.cogTest_Pass_witch_contact_are_we_looking_for_pass
            ),
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_contacts_page_second_assist,
                audioUrlRes = Res.string.cogTest_Pass_search_for_hana_choen_in_contacts_pass
            ),
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_contacts_page_thired_assist,
                audioUrlRes = Res.string.cogTest_Pass_search_at_latter_h_pass
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PassContactsScreen(
            state = state,
            onAction = { action ->
                touchCounter++
                viewModel.onAction(action)
            }
        )

        if (state.showTimeoutDialog) {
            val index = (state.errorCount - 1).coerceIn(0, mediations.lastIndex)
            val currentMediation = mediations[index]

            PassMediationDialog(
                description = stringResource(currentMediation.descriptionRes),
                audioUrl = stringResource(currentMediation.audioUrlRes),
                countdownSeconds = 10,
                onDismiss = {
                    viewModel.onAction(PassContactsAction.OnTimeoutDialogDismiss)
                }
            )
        }
    }
}

@Composable
fun PassContactsScreen(
    state: PassContactsState,
    onAction: (PassContactsAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val allContacts = stringArrayResource(Res.array.cogTest_Pass_person_names)

    val filteredContacts = remember(state.searchQuery, allContacts) {
        if (state.searchQuery.isEmpty()) {
            allContacts
        } else {
            allContacts.filter { it.contains(state.searchQuery, ignoreCase = true) }
        }
    }

    DmtBaseScreen(
        titleText = "Contacts",
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { onAction(PassContactsAction.OnSearchQueryChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary, // Or Color(0xFF26A69A)
                        unfocusedBorderColor = Color.LightGray
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredContacts) { contactName ->
                        val initial = contactName.firstOrNull()?.toString() ?: ""

                        PassContactItem(
                            name = contactName,
                            initial = initial,
                            onClick = { onAction(PassContactsAction.OnContactClick(contactName)) },
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun PassContactsPreview() {
    DmtTheme {
        PassContactsScreen(
            state = PassContactsState(),
            onAction = {}
        )
    }
}