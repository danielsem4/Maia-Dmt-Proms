package maia.dmt.orientation.presentation.draw

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.orientation.presentation.generated.resources.Res
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_back_to_task
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_clear_all
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_clear_drawing_description
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_clear_drawing_title
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_draw
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_draw_cancel
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_draw_title
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_erase
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_next
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_touch_warning
import dmtproms.cogtest.orientation.presentation.generated.resources.cog_orientation_trial_draw_instructions
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.canvas.DrawingCanvas
import maia.dmt.core.designsystem.components.canvas.DrawingController
import maia.dmt.core.designsystem.components.canvas.rememberDrawingController
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.model.canvas.DrawingCanvasConfig
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DrawOrientationRoot(
    onNavigateToNext: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: DrawOrientationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawingController = rememberDrawingController()

    LaunchedEffect(drawingController.hasDrawings()) {
        if (drawingController.hasDrawings()) {
            viewModel.onAction(DrawOrientationAction.OnDrawingStarted)
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is DrawOrientationEvent.NavigateToNext -> {
                    // CHANGE: Removed captureBitmap() here. It is now done in the button click.
                    onNavigateToNext()
                }
                is DrawOrientationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    DrawOrientationScreen(
        state = state,
        drawingController = drawingController,
        onAction = { action ->
            when (action) {
                is DrawOrientationAction.OnConfirmClearAll -> {
                    drawingController.clearAll()
                    viewModel.onAction(action)
                }
                is DrawOrientationAction.OnToggleDrawMode -> {
                    drawingController.toggleEraseMode()
                }
                is DrawOrientationAction.OnUndoClick -> {
                    drawingController.undo()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun DrawOrientationScreen(
    state: DrawOrientationState = DrawOrientationState(),
    drawingController: DrawingController = rememberDrawingController(),
    onAction: (DrawOrientationAction) -> Unit = {}
) {
    if (state.showInactivityDialog) {
        DmtInfoDialog(
            title = "Are you still there?",
            description = "Click to continue",
            confirmButtonText = stringResource(Res.string.cog_orientation_back_to_task),
            onConfirmClick = { onAction(DrawOrientationAction.OnBackToTask) },
            onDismiss = { onAction(DrawOrientationAction.OnDismissInactivityDialog) }
        )
    }

    if (state.showClearAllDialog) {
        DmtConfirmationDialog(
            title = stringResource(Res.string.cog_orientation_clear_drawing_title),
            description = stringResource(Res.string.cog_orientation_clear_drawing_description),
            confirmButtonText = stringResource(Res.string.cog_orientation_clear_all),
            cancelButtonText = stringResource(Res.string.cog_orientation_draw_cancel),
            onConfirmClick = { onAction(DrawOrientationAction.OnConfirmClearAll) },
            onCancelClick = { onAction(DrawOrientationAction.OnDismissClearAllDialog) },
            onDismiss = { onAction(DrawOrientationAction.OnDismissClearAllDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cog_orientation_draw_title),
        onIconClick = { onAction(DrawOrientationAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.cog_orientation_trial_draw_instructions),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                DrawingCanvas(
                    controller = drawingController,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    config = DrawingCanvasConfig(),
                    hintText = stringResource(Res.string.cog_orientation_touch_warning)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DmtButton(
                        text = stringResource(Res.string.cog_orientation_clear_all),
                        onClick = { onAction(DrawOrientationAction.OnClearAllClick) },
                        style = DmtButtonStyle.PRIMARY,
                        enabled = drawingController.hasDrawings()
                    )

                    // CHANGE: Capture happens here immediately on click
                    DmtButton(
                        text = stringResource(Res.string.cog_orientation_next),
                        onClick = {
                            val bitmap = drawingController.captureBitmap()
                            onAction(DrawOrientationAction.OnNextClick(bitmap))
                        },
                        enabled = true
                    )

                    DmtButton(
                        text = if (drawingController.isErasing) {
                            stringResource(Res.string.cog_orientation_draw)
                        } else {
                            stringResource(Res.string.cog_orientation_erase)
                        },
                        onClick = { onAction(DrawOrientationAction.OnToggleDrawMode) },
                        style = if (drawingController.isErasing) {
                            DmtButtonStyle.PRIMARY
                        } else {
                            DmtButtonStyle.SECONDARY
                        }
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun DrawOrientationPreview() {
    DmtTheme {
        DrawOrientationScreen()
    }
}