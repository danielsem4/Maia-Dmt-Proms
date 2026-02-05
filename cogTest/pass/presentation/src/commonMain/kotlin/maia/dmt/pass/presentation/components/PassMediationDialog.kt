package maia.dmt.pass.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.pass_attention
import kotlinx.coroutines.delay
import maia.dmt.core.designsystem.components.animations.AnimatedSpeaker
import maia.dmt.core.designsystem.components.dialogs.DmtCustomDialog
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PassMediationDialog(
    description: String,
    audioUrl: String,
    countdownSeconds: Int = 10,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlayingAudio by remember { mutableStateOf(true) }
    var remainingSeconds by remember { mutableStateOf(countdownSeconds) }
    var showCloseButton by remember { mutableStateOf(false) }

    val soundPlayer = rememberSoundPlayer(
        onCompletion = {
            isPlayingAudio = false
            showCloseButton = true
        }
    )

    LaunchedEffect(audioUrl) {
        soundPlayer.play(audioUrl)
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.stop()
        }
    }

    LaunchedEffect(isPlayingAudio) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
        if (remainingSeconds == 0) {
            onDismiss()
        }
    }

    DmtCustomDialog(
        onDismiss = onDismiss,
        showCloseButton = false,
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)

        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-40).dp)
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.pass_attention),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            if (isPlayingAudio) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 8.dp, top = 8.dp)
                ) {
                    AnimatedSpeaker(
                        speed = 1.0f,
                        imageSet = 48.dp
                    )
                }
            }

            if (showCloseButton) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 4.dp, top = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.extended.textSecondary
                            )
                        }
                        if (remainingSeconds > 0) {
                            Text(
                                text = remainingSeconds.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.extended.textSecondary,
                                modifier = Modifier.offset(y = (-8).dp)
                            )
                        }
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview
fun PassMediationDialogPreview() {
    DmtTheme {
        PassMediationDialog(
            description = "מה עליך לעשות עכשיו?",
            audioUrl = "",
            onDismiss = {}
        )
    }
}