package maia.dmt.hitber.presentation.hitberThiredQuestion.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_good_job
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_start
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReactionCard(
    isPlaying: Boolean,
    isFinished: Boolean,
    currentNumber: Int?,
    onStartClick: () -> Unit,
    onPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCardPressed by remember { mutableStateOf(false) }

    val cardColor by animateColorAsState(
        targetValue = if (isCardPressed && isPlaying)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(100),
        label = "cardColor",
    )

    val contentColor by animateColorAsState(
        targetValue = if (isCardPressed && isPlaying)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurface,
        label = "contentColor",
    )

    Card(
        modifier = modifier
            .pointerInput(isPlaying) {
                if (!isPlaying) return@pointerInput
                detectTapGestures(
                    onPress = {
                        isCardPressed = true
                        onPress()
                        tryAwaitRelease()
                        isCardPressed = false
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when {
                !isPlaying && !isFinished -> {
                    DmtButton(
                        text = stringResource(Res.string.cogTest_hitber_start),
                        onClick = onStartClick,
                        style = DmtButtonStyle.PRIMARY,
                    )
                }

                isPlaying -> {
                    currentNumber?.let { number ->
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                            ),
                            fontWeight = FontWeight.Bold,
                            color = contentColor,
                        )
                    }
                }

                isFinished -> {
                    Text(
                        text = stringResource(Res.string.cogTest_hitber_good_job),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
