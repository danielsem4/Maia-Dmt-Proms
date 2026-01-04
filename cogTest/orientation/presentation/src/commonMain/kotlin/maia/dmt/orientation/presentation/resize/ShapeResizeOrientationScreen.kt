package maia.dmt.orientation.presentation.resize

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_back_to_task
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_next
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_shape_resize_title
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_trial_pinch_instructions
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.shapeDraw.DmtShape
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.orientation.domain.model.DragShape
import maia.dmt.orientation.presentation.components.AnimatedPinchGesture
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ShapeResizeOrientationRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ShapeResizeOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is ShapeResizeOrientationEvent.NavigateToNext -> onNavigateToNext()
                is ShapeResizeOrientationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    ShapeResizeOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ShapeResizeOrientationScreen(
    state: ShapeResizeOrientationState = ShapeResizeOrientationState(),
    onAction: (ShapeResizeOrientationAction) -> Unit = {}
) {
    if (state.showInactivityDialog) {
        DmtInfoDialog(
            title = "Are you still there?",
            description = "Click to continue",
            confirmButtonText = stringResource(Res.string.cog_orientation_back_to_task),
            onConfirmClick = { onAction(ShapeResizeOrientationAction.OnBackToTask) },
            onDismiss = { onAction(ShapeResizeOrientationAction.OnDismissInactivityDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_shape_resize_title),
        onIconClick = { onAction(ShapeResizeOrientationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(Res.string.cog_orientation_trial_pinch_instructions),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )

                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.2f)
                            .padding(24.dp)
                            .border(4.dp, Color.Red)
                            .onGloballyPositioned { coordinates ->
                                onAction(ShapeResizeOrientationAction.OnTargetPositioned(coordinates.boundsInRoot()))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        ResizableShape(
                            shape = state.targetShape,
                            scale = state.currentScale,
                            onScaleChange = { newScale ->
                                onAction(ShapeResizeOrientationAction.OnScaleChange(newScale))
                            }
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedPinchGesture(
                            modifier = Modifier.fillMaxSize(0.7f),
                            speed = 0.75f
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp, top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DmtButton(
                        text = stringResource(Res.string.cog_orientation_next),
                        onClick = { onAction(ShapeResizeOrientationAction.OnNextClick) }
                    )
                }
            }
        }
    )
}

@Composable
fun ResizableShape(
    shape: DragShape,
    scale: Float,
    onScaleChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentScale by remember { mutableStateOf(scale) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    currentScale = (currentScale * zoom).coerceIn(0.5f, 3f)
                    onScaleChange(currentScale)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        DmtShape(
            shapeKey = shape.key,
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    scaleX = currentScale
                    scaleY = currentScale
                },
            color = Color(0xFF008EAE)
        )
    }
}

@Composable
@Preview
fun ShapeResizeOrientationPreview() {
    DmtTheme {
        ShapeResizeOrientationScreen()
    }
}
