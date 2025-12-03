package maia.dmt.cdt.presentation.cdtDraw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.cdt.presentation.generated.resources.Res
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_cancel
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_clear_all
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_clear_drawing_description
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_clear_drawing_title
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_draw
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_drawing_test_title
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_erase
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_instruction_draw_1110
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_next_question
import dmtproms.cogtest.cdt.presentation.generated.resources.cogTest_cdt_touch_warning
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.canvas.DrawingCanvas
import maia.dmt.core.designsystem.components.canvas.DrawingController
import maia.dmt.core.designsystem.components.canvas.rememberDrawingController
import maia.dmt.core.designsystem.components.dialogs.DmtConfirmationDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.model.canvas.DrawingCanvasConfig
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CdtDrawRoot(
    onNavigateToNextQuestion: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CdtDrawViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawingController = rememberDrawingController()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is CdtDrawEvent.NavigateToNextQuestion -> {
                    drawingController.captureBitmap()?.let { bitmap ->
                        viewModel.saveDrawingBitmap(bitmap)
                    }
                    onNavigateToNextQuestion()
                }
                is CdtDrawEvent.NavigateBack -> onNavigateBack()
                is CdtDrawEvent.ShowError -> {

                }
            }
        }
    }

    CdtDrawScreen(
        state = state,
        drawingController = drawingController,
        onAction = { action ->
            when (action) {
                is CdtDrawAction.OnConfirmClearAll -> {
                    drawingController.clearAll()
                    viewModel.onAction(action)
                }
                is CdtDrawAction.OnToggleDrawMode -> {
                    drawingController.toggleEraseMode()
                }
                is CdtDrawAction.OnUndoClick -> {
                    drawingController.undo()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun CdtDrawScreen(
    state: CdtDrawState,
    drawingController: DrawingController,
    onAction: (CdtDrawAction) -> Unit
) {
    if (state.showClearAllDialog) {
        DmtConfirmationDialog(
            title = stringResource(Res.string.cogTest_cdt_clear_drawing_title),
            description = stringResource(Res.string.cogTest_cdt_clear_drawing_description),
            confirmButtonText = stringResource(Res.string.cogTest_cdt_clear_all),
            cancelButtonText = stringResource(Res.string.cogTest_cdt_cancel),
            onConfirmClick = { onAction(CdtDrawAction.OnConfirmClearAll) },
            onCancelClick = { onAction(CdtDrawAction.OnDismissClearAllDialog) },
            onDismiss = { onAction(CdtDrawAction.OnDismissClearAllDialog) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_cdt_drawing_test_title),
        onIconClick = { onAction(CdtDrawAction.OnBackClick) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.cogTest_cdt_instruction_draw_1110),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                DrawingCanvas(
                    controller = drawingController,
                    modifier = Modifier
                        .weight(0.9f)
                        .fillMaxWidth(),
                    config = DrawingCanvasConfig(),
                    hintText = stringResource(Res.string.cogTest_cdt_touch_warning)
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
                        text = stringResource(Res.string.cogTest_cdt_clear_all),
                        onClick = { onAction(CdtDrawAction.OnClearAllClick) },
                        style = DmtButtonStyle.PRIMARY,
                        enabled = drawingController.hasDrawings()
                    )

                    DmtButton(
                        text = stringResource(Res.string.cogTest_cdt_next_question),
                        onClick = { onAction(CdtDrawAction.OnNextQuestionClick) },
                        enabled = true
                    )

                    DmtButton(
                        text = if (drawingController.isErasing) {
                            stringResource(Res.string.cogTest_cdt_draw)
                        } else {
                            stringResource(Res.string.cogTest_cdt_erase)
                        },
                        onClick = { onAction(CdtDrawAction.OnToggleDrawMode) },
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
fun CdtDrawPreview() {
    DmtTheme {
        CdtDrawScreen(
            state = CdtDrawState(),
            drawingController = rememberDrawingController(),
            onAction = {}
        )
    }
}