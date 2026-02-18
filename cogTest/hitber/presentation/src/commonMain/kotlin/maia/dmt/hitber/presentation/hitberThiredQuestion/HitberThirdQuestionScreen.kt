package maia.dmt.hitber.presentation.hitberThiredQuestion

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_good_job
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_start
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_third_mission_instructions
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberThirdQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberThirdQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberThirdQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberThirdQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HitberThirdQuestionScreen(
    state: HitberThirdQuestionState,
    onAction: (HitberThirdQuestionAction) -> Unit,
) {
    var isCardPressed by remember { mutableStateOf(false) }

    val cardColor by animateColorAsState(
        targetValue = if (isCardPressed && state.isPlaying)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(100),
        label = "cardColor",
    )

    val contentColor by animateColorAsState(
        targetValue = if (isCardPressed && state.isPlaying)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurface,
        label = "contentColor"
    )

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_title),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.cogTest_hitber_third_mission_instructions),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .pointerInput(state.isPlaying) {
                            if (!state.isPlaying) return@pointerInput

                            detectTapGestures(
                                onPress = {
                                    isCardPressed = true
                                    onAction(HitberThirdQuestionAction.OnCardPress)
                                    tryAwaitRelease()
                                    isCardPressed = false
                                }
                            )
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor,
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        when {
                            !state.isPlaying && !state.isFinished -> {
                                DmtButton(
                                    text = stringResource(Res.string.cogTest_hitber_start),
                                    onClick = { onAction(HitberThirdQuestionAction.OnStartClick) },
                                    style = DmtButtonStyle.PRIMARY,
                                )
                            }

                            state.isPlaying -> {
                                state.currentNumber?.let { number ->
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

                            state.isFinished -> {
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

                if (state.isFinished) {
                    Spacer(modifier = Modifier.height(16.dp))
                    DmtButton(
                        text = stringResource(Res.string.cogTest_hitber_next),
                        onClick = { onAction(HitberThirdQuestionAction.OnNextClick) },
                        style = DmtButtonStyle.PRIMARY,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun HitberThirdQuestionPreview() {
    DmtTheme {
        HitberThirdQuestionScreen(
            state = HitberThirdQuestionState(),
            onAction = {},
        )
    }
}