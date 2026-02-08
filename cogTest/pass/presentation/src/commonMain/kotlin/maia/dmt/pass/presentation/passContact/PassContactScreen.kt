package maia.dmt.pass.presentation.passContact

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_call
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contact_info
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contact_page_first_assist
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_contacts
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_hana_cohen
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_message
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_phone_number
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_press_the_number_or_the_dial_button_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_video
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_do_you_need_to_do_pass
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_what_you_need_to_do
import dmtproms.cogtest.pass.presentation.generated.resources.cogTest_Pass_whatsapp
import dmtproms.cogtest.pass.presentation.generated.resources.pass_message
import dmtproms.cogtest.pass.presentation.generated.resources.pass_phone
import dmtproms.cogtest.pass.presentation.generated.resources.pass_video
import dmtproms.cogtest.pass.presentation.generated.resources.pass_whatsapp
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.inactivity.InactivityHandler
import maia.dmt.pass.presentation.components.ContactActionItem
import maia.dmt.pass.presentation.components.PassMediationDialog
import maia.dmt.pass.presentation.model.MediationConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PassContactRoot(
    onNavigateToNext: () -> Unit,
    viewModel: PassContactViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val inactivityHandler = remember(scope) {
        InactivityHandler(
            scope = scope,
            timeoutMs = 15_000L,
            onInactivityTimeout = { viewModel.onAction(PassContactAction.OnTimeout) }
        )
    }

    DisposableEffect(state.showTimeoutDialog) {
        if (!state.showTimeoutDialog) {
            inactivityHandler.start()
        }
        onDispose { inactivityHandler.cancel() }
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
                PassContactEvent.NavigateToNextScreen -> onNavigateToNext()
            }
        }
    }

    val mediations = remember {
        listOf(
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_what_you_need_to_do,
                audioUrlRes = Res.string.cogTest_Pass_what_do_you_need_to_do_pass
            ),
            MediationConfig(
                descriptionRes = Res.string.cogTest_Pass_contact_page_first_assist,
                audioUrlRes = Res.string.cogTest_Pass_press_the_number_or_the_dial_button_pass
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PassContactScreen(
            onAction = { action ->
                touchCounter++
                viewModel.onAction(action)
            }
        )

        if (state.showTimeoutDialog) {
            val index = (state.totalErrors - 1).coerceIn(0, mediations.lastIndex)
            val currentMediation = mediations[index]

            PassMediationDialog(
                description = stringResource(currentMediation.descriptionRes),
                audioUrl = stringResource(currentMediation.audioUrlRes),
                countdownSeconds = 10,
                onDismiss = {
                    viewModel.onAction(PassContactAction.OnTimeoutDialogDismiss)
                }
            )
        }
    }
}

@Composable
fun PassContactScreen(
    onAction: (PassContactAction) -> Unit
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_Pass_contacts),
        onIconClick = { },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val name = stringResource(Res.string.cogTest_Pass_hana_cohen)
                    val initial = if (name.isNotEmpty()) name[0] else ' '
                    Text(
                        text = initial.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.cogTest_Pass_hana_cohen),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ContactActionItem(
                        icon = painterResource(Res.drawable.pass_video),
                        text = stringResource(Res.string.cogTest_Pass_video),
                        onClick = { onAction(PassContactAction.OnWrongPress("Top Video")) }
                    )
                    ContactActionItem(
                        icon = painterResource(Res.drawable.pass_message),
                        text = stringResource(Res.string.cogTest_Pass_message),
                        onClick = { onAction(PassContactAction.OnWrongPress("Top Message")) }
                    )
                    ContactActionItem(
                        icon = painterResource(Res.drawable.pass_phone),
                        text = stringResource(Res.string.cogTest_Pass_call),
                        onClick = { onAction(PassContactAction.OnCallClick) }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.cogTest_Pass_contact_info),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.End),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(Res.drawable.pass_video),
                                    contentDescription = null,
                                    tint = Color.DarkGray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) { onAction(PassContactAction.OnWrongPress("Row Video")) }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    painter = painterResource(Res.drawable.pass_message),
                                    contentDescription = null,
                                    tint = Color.DarkGray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) { onAction(PassContactAction.OnWrongPress("Row Message")) }
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { onAction(PassContactAction.OnCallClick) }
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = stringResource(Res.string.cogTest_Pass_phone_number),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = stringResource(Res.string.cogTest_Pass_call),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    painter = painterResource(Res.drawable.pass_phone),
                                    contentDescription = null,
                                    tint = Color.DarkGray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onAction(PassContactAction.OnWrongPress("WhatsApp")) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End // Align to end for consistent look
                        ) {
                            Text(
                                text = stringResource(Res.string.cogTest_Pass_whatsapp),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFF25D366), CircleShape)
                                    .padding(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.pass_whatsapp),
                                    contentDescription = "WhatsApp",
                                    tint = Color.White,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}