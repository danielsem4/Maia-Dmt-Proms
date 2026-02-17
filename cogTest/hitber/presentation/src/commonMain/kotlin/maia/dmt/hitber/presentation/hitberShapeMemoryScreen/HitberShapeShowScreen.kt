package maia.dmt.hitber.presentation.hitberShapeMemoryScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_Select_5_shapes
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_memory_shape_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_observe_the_pictures_until_the_time_is_up
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_shape_dialog_mission_instructions
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_start
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.shapeDraw.DmtShape
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.hitber.domain.model.HitberShape
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HitberShapeShowRoot(
    viewModel: HitberShapeShowViewModel = viewModel { HitberShapeShowViewModel() },
    onNavigateNext: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.action) {
        viewModel.action.collect { action ->
            when (action) {
                HitberShapeShowAction.NavigateNext -> onNavigateNext()
            }
        }
    }

    HitberShapeShowScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun HitberShapeShowScreen(
    state: HitberShapeShowState,
    onEvent: (HitberShapeShowEvent) -> Unit
) {
    if (state.showInfoDialog) {
        DmtInfoDialog(
            title = stringResource(Res.string.cogTest_hitber_Select_5_shapes),
            description = stringResource(Res.string.cogTest_hitber_shape_dialog_mission_instructions),
            confirmButtonText = stringResource(Res.string.cogTest_hitber_next),
            onConfirmClick = { onEvent(HitberShapeShowEvent.OnConfirmDialog) },
            onDismiss = { onEvent(HitberShapeShowEvent.OnDialogDismiss) }
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_memory_shape_title),
        onIconClick = { },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(Res.string.cogTest_hitber_observe_the_pictures_until_the_time_is_up),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.extended.textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.selectedShapes.forEach { shape ->
                            DmtShape(
                                shapeKey = shape.registryKey,
                                modifier = Modifier.size(80.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "00:${state.timerSeconds.toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.extended.textPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.1f)
                        )

                        DmtButton(
                            text = stringResource(Res.string.cogTest_hitber_next),
                            onClick = { onEvent(HitberShapeShowEvent.OnNextClick) },
                            style = DmtButtonStyle.PRIMARY,
                        )

                        Spacer(modifier = Modifier.weight(0.1f))
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun HitberShapeShowPreview() {
    DmtTheme {
        HitberShapeShowScreen(
            state = HitberShapeShowState(
                selectedShapes = listOf(
                    HitberShape.STAR,
                    HitberShape.RHOMBUS,
                    HitberShape.HEXAGON,
                    HitberShape.X,
                    HitberShape.PENTAGON
                ),
                showInfoDialog = false,
                timerSeconds = 19
            ),
            onEvent = {}
        )
    }
}