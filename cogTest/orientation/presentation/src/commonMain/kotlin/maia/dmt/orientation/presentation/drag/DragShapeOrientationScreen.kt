package maia.dmt.orientation.presentation.drag

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_back_to_task
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_next
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_shape_drag_title
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_trial_drag_instructions
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.shapeDraw.DmtShape
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.orientation.domain.model.DragShape
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DragShapeOrientationRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: DragShapeOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is DragShapeOrientationEvent.NavigateToNext -> onNavigateToNext()
                is DragShapeOrientationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    DragShapeOrientationScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun DragShapeOrientationScreen(
    state: DragShapeOrientationState,
    onAction: (DragShapeOrientationAction) -> Unit
) {
    if (state.showInactivityDialog) {
        DmtInfoDialog(
            title = "Are you still there?",
            description = "Click to continue",
            confirmButtonText = stringResource(Res.string.cog_orientation_back_to_task),
            onConfirmClick = { onAction(DragShapeOrientationAction.OnBackToTask) },
            onDismiss = { onAction(DragShapeOrientationAction.OnDismissInactivityDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_shape_drag_title),
        onIconClick = { onAction(DragShapeOrientationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween

            ) {

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(Res.string.cog_orientation_trial_drag_instructions),
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
                                onAction(DragShapeOrientationAction.OnTargetPositioned(coordinates.boundsInRoot()))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.currentShapeInBox != DragShape.NONE) {
                            ShapeIcon(
                                shape = state.currentShapeInBox,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val shapes = listOf(
                            Pair(DragShape.STAR, DragShape.TRIANGLE),
                            Pair(DragShape.ASTERISK, DragShape.CIRCLE),
                            Pair(DragShape.DIAMOND, DragShape.SQUARE)
                        )

                        shapes.forEach { (shape1, shape2) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                DraggableShapeItem(shape = shape1, state = state, onAction = onAction)
                                DraggableShapeItem(shape = shape2, state = state, onAction = onAction)
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DmtButton(
                        text = stringResource(Res.string.cog_orientation_next),
                        onClick = { onAction(DragShapeOrientationAction.OnNextClick) }
                    )
                }
            }
        }
    )
}

@Composable
fun DraggableShapeItem(
    shape: DragShape,
    state: DragShapeOrientationState,
    onAction: (DragShapeOrientationAction) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val isBeingDragged = state.currentlyDraggedShape == shape
    val rawOffset = if (isBeingDragged) state.dragOffset else androidx.compose.ui.geometry.Offset.Zero

    val resolvedX = if (layoutDirection == LayoutDirection.Rtl) -rawOffset.x else rawOffset.x
    var currentBounds by remember { mutableStateOf(Rect.Zero) }

    Box(
        modifier = Modifier
            .size(100.dp)
            .offset { IntOffset(resolvedX.roundToInt(), rawOffset.y.roundToInt()) }
            .zIndex(if (isBeingDragged) 10f else 0f)
            .onGloballyPositioned { currentBounds = it.boundsInRoot() }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        onAction(DragShapeOrientationAction.OnDragStart(shape))
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onAction(DragShapeOrientationAction.OnDragDelta(dragAmount))
                    },
                    onDragEnd = {
                        onAction(DragShapeOrientationAction.OnDragEnd(currentBounds))
                    },
                    onDragCancel = {
                        onAction(DragShapeOrientationAction.OnDragEnd(Rect.Zero))
                    }
                )
            }
    ) {
        ShapeIcon(shape = shape, modifier = Modifier.fillMaxSize().padding(12.dp))
    }
}

@Composable
fun ShapeIcon(shape: DragShape, modifier: Modifier = Modifier) {
    if (shape != DragShape.NONE) {
        DmtShape(
            shapeKey = shape.key,
            modifier = modifier,
            color = Color(0xFF008EAE)
        )
    }
}

@Composable
@Preview
fun DragShapeOrientationPreview() {
    DmtTheme {
        DragShapeOrientationScreen(
            state = DragShapeOrientationState(),
            onAction = {}
        )
    }
}