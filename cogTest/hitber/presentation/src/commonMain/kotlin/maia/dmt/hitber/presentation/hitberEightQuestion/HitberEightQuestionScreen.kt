package maia.dmt.hitber.presentation.hitberEightQuestion

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_eight_ball_instruction_black
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_eight_ball_instruction_green
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_eight_ball_instruction_red
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_eight_ball_instruction_yellow
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_eight_ball_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.hitber.presentation.hitberEightQuestion.components.BALL_SIZE_DP
import maia.dmt.hitber.presentation.hitberEightQuestion.components.DraggableBall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun HitberEightQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberEightQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberEightQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberEightQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HitberEightQuestionScreen(
    state: HitberEightQuestionState,
    onAction: (HitberEightQuestionAction) -> Unit,
) {
    val instructionText = when (state.targetBallColor) {
        BallColor.RED -> stringResource(Res.string.cogTest_hitber_eight_ball_instruction_red)
        BallColor.BLACK -> stringResource(Res.string.cogTest_hitber_eight_ball_instruction_black)
        BallColor.GREEN -> stringResource(Res.string.cogTest_hitber_eight_ball_instruction_green)
        BallColor.YELLOW -> stringResource(Res.string.cogTest_hitber_eight_ball_instruction_yellow)
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_eight_ball_title),
        onIconClick = {},
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = instructionText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                )

                val density = LocalDensity.current
                val ballSizePx = with(density) { BALL_SIZE_DP.toPx() }

                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .onGloballyPositioned { coords ->
                            onAction(
                                HitberEightQuestionAction.OnContainerPositioned(
                                    coords.positionInRoot()
                                )
                            )
                        },
                ) {
                    val maxWidthPx = with(density) { maxWidth.toPx() }
                    val maxHeightPx = with(density) { maxHeight.toPx() }

                    LaunchedEffect(maxWidthPx, maxHeightPx) {
                        onAction(
                            HitberEightQuestionAction.OnLayoutReady(
                                containerWidth = maxWidthPx,
                                containerHeight = maxHeightPx,
                                ballSizePx = ballSizePx,
                            )
                        )
                    }

                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )

                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight()
                                .padding(vertical = 32.dp, horizontal = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    // Fixed size applied here: 2.5 times the size of the ball
                                    .size(BALL_SIZE_DP * 2.5f)
                                    .border(
                                        width = 5.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(12.dp),
                                    )
                                    .onGloballyPositioned { coords ->
                                        onAction(
                                            HitberEightQuestionAction.OnDropZonePositioned(
                                                coords.boundsInRoot()
                                            )
                                        )
                                    },
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }

                    Box(modifier = Modifier.matchParentSize()) {
                        state.balls.forEach { ball ->
                            DraggableBall(
                                ball = ball,
                                onDrag = { dragAmount ->
                                    onAction(HitberEightQuestionAction.OnBallDrag(ball.id, dragAmount))
                                },
                                onDragEnd = {
                                    onAction(HitberEightQuestionAction.OnBallDrop(ball.id))
                                },
                                modifier = Modifier.offset {
                                    IntOffset(
                                        x = ball.currentOffset.x.roundToInt(),
                                        y = ball.currentOffset.y.roundToInt(),
                                    )
                                },
                            )
                        }
                    }
                }
                if (state.isCompleted) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        DmtButton(
                            text = stringResource(Res.string.cogTest_hitber_next),
                            onClick = { onAction(HitberEightQuestionAction.OnNextClick) },
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun HitberEightQuestionPreview() {
    DmtTheme {
        HitberEightQuestionScreen(
            state = HitberEightQuestionState(targetBallColor = BallColor.RED),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun HitberEightQuestionCompletedPreview() {
    DmtTheme {
        HitberEightQuestionScreen(
            state = HitberEightQuestionState(
                targetBallColor = BallColor.GREEN,
                isCompleted = true,
                droppedBallColor = BallColor.GREEN,
            ),
            onAction = {},
        )
    }
}