package maia.dmt.hitber.presentation.hitberSecondQuestion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_got_it
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_memory_shape_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_please_select_the_5_shapes
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_shape_mission_instructions
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_the_shapes
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.buttons.DmtButtonStyle
import maia.dmt.core.designsystem.components.dialogs.DmtInfoDialog
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.shapeDraw.DmtShape
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.hitber.domain.model.HitberShape
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HitberSecondQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberSecondQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberSecondQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberSecondQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HitberSecondQuestionScreen(
    state: HitberSecondQuestionState,
    onAction: (HitberSecondQuestionAction) -> Unit,
) {
    val deviceConfig = currentDeviceConfiguration()

    val itemsPerRow = when (deviceConfig) {
        DeviceConfiguration.MOBILE_LANDSCAPE,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 5
        else -> 2
    }

    if (state.showErrorDialog) {
        DmtInfoDialog(
            title = stringResource(Res.string.cogTest_hitber_the_shapes),
            description = stringResource(Res.string.cogTest_hitber_shape_mission_instructions),
            confirmButtonText = stringResource(Res.string.cogTest_hitber_got_it),
            onConfirmClick = { onAction(HitberSecondQuestionAction.OnErrorDialogDismiss) },
            onDismiss = { onAction(HitberSecondQuestionAction.OnErrorDialogDismiss) },
        )
    }

    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_memory_shape_title),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.cogTest_hitber_please_select_the_5_shapes),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.weight(1f))

                ShapeGrid(
                    visibleShapes = state.visibleShapes,
                    selectedShapes = state.selectedShapes,
                    itemsPerRow = itemsPerRow,
                    onShapeClick = { shape ->
                        onAction(HitberSecondQuestionAction.OnShapeClick(shape))
                    },
                )

                Spacer(modifier = Modifier.weight(1f))

                DmtButton(
                    text = stringResource(Res.string.cogTest_hitber_next),
                    onClick = { onAction(HitberSecondQuestionAction.OnNextClick) },
                    style = DmtButtonStyle.PRIMARY,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                )
            }
        }
    )
}

@Composable
private fun ShapeGrid(
    visibleShapes: List<HitberShape>,
    selectedShapes: Set<HitberShape>,
    itemsPerRow: Int,
    onShapeClick: (HitberShape) -> Unit,
) {
    val rows = visibleShapes.chunked(itemsPerRow)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        rows.forEach { rowShapes ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowShapes.forEach { shape ->
                    ShapeItem(
                        shape = shape,
                        isSelected = shape in selectedShapes,
                        onClick = { onShapeClick(shape) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ShapeItem(
    shape: HitberShape,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val cornerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(cornerShape)
            .then(
                if (isSelected) {
                    Modifier.background(primaryColor, cornerShape)
                } else {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = cornerShape,
                    )
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        DmtShape(
            shapeKey = shape.registryKey,
            modifier = Modifier.size(80.dp), // Adjusted inner size
            color = if (isSelected) Color.White else primaryColor,
            strokeWidth = 3.dp,
        )
    }
}

@Composable
@Preview
fun HitberSecondQuestionPreview() {
    DmtTheme {
        HitberSecondQuestionScreen(
            state = HitberSecondQuestionState(
                visibleShapes = HitberShape.entries.toList(),
                selectedShapes = setOf(HitberShape.STAR, HitberShape.HEXAGON),
            ),
            onAction = {},
        )
    }
}
