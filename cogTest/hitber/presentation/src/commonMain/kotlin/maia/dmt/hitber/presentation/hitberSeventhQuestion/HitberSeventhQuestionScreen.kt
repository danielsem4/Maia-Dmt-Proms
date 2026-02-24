package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_drag_and_drop_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_listen
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_fridge
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_napkin
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_open_fridge
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_speaker
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_table
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import maia.dmt.hitber.presentation.hitberSeventhQuestion.components.DraggableItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun HitberSeventhQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberSeventhQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val soundPlayer = rememberSoundPlayer(onCompletion = {})

    LaunchedEffect(state.instructionUrl) {
        if (state.instructionUrl.isNotBlank()) {
            soundPlayer.play(state.instructionUrl)
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberSeventhQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberSeventhQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
        onListenClick = {
            if (state.instructionUrl.isNotBlank()) {
                soundPlayer.play(state.instructionUrl)
            }
        },
    )
}

@Composable
fun HitberSeventhQuestionScreen(
    state: HitberSeventhQuestionState,
    onAction: (HitberSeventhQuestionAction) -> Unit,
    onListenClick: () -> Unit = {},
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_drag_and_drop_title),
        onIconClick = {},
        content = {
            Column(modifier = Modifier.fillMaxSize()) {

                // Listen button row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    DmtButton(
                        text = stringResource(Res.string.cogTest_hitber_listen),
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.hitber_speaker),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        onClick = onListenClick,
                    )
                }

                // Game area
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .onGloballyPositioned { coords ->
                            onAction(
                                HitberSeventhQuestionAction.OnContainerPositioned(
                                    coords.positionInRoot()
                                )
                            )
                        },
                ) {
                    val density = LocalDensity.current
                    val maxWidthPx = with(density) { maxWidth.toPx() }
                    val maxHeightPx = with(density) { maxHeight.toPx() }
                    val fridgeCenterX = maxWidthPx / 4f

                    LaunchedEffect(maxWidthPx, maxHeightPx) {
                        onAction(
                            HitberSeventhQuestionAction.OnLayoutReady(
                                fridgeCenterX = fridgeCenterX,
                                screenHeightPx = maxHeightPx,
                            )
                        )
                    }

                    // Layer 1: Fridge (left half) + Table with napkins (right half)
                    Row(modifier = Modifier.fillMaxSize()) {

                        // Left: Fridge — click to open/close
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onAction(HitberSeventhQuestionAction.OnFridgeClick) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(
                                    if (state.isFridgeOpen) Res.drawable.hitber_open_fridge
                                    else Res.drawable.hitber_fridge
                                ),
                                contentDescription = "Fridge",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit,
                            )
                        }

                        // Right: Table with four colored napkins
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.hitber_table),
                                contentDescription = "Table",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit,
                            )

                            // 2×2 napkin grid placed on the table
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    NapkinView(
                                        color = NapkinColor.RED,
                                        onPositioned = { rect ->
                                            onAction(HitberSeventhQuestionAction.OnNapkinPositioned(NapkinColor.RED, rect))
                                        },
                                    )
                                    NapkinView(
                                        color = NapkinColor.GREEN,
                                        onPositioned = { rect ->
                                            onAction(HitberSeventhQuestionAction.OnNapkinPositioned(NapkinColor.GREEN, rect))
                                        },
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    NapkinView(
                                        color = NapkinColor.BLUE,
                                        onPositioned = { rect ->
                                            onAction(HitberSeventhQuestionAction.OnNapkinPositioned(NapkinColor.BLUE, rect))
                                        },
                                    )
                                    NapkinView(
                                        color = NapkinColor.YELLOW,
                                        onPositioned = { rect ->
                                            onAction(HitberSeventhQuestionAction.OnNapkinPositioned(NapkinColor.YELLOW, rect))
                                        },
                                    )
                                }
                            }
                        }
                    }

                    // Layer 2: Draggable items overlaid on the entire game area
                    Box(modifier = Modifier.matchParentSize()) {
                        state.items.forEach { item ->
                            DraggableItem(
                                item = item,
                                isFridgeOpen = state.isFridgeOpen,
                                onDrag = { dragAmount ->
                                    onAction(HitberSeventhQuestionAction.OnItemDrag(item.id, dragAmount))
                                },
                                onDragEnd = {
                                    onAction(HitberSeventhQuestionAction.OnItemDrop(item.id))
                                },
                                modifier = Modifier.offset {
                                    IntOffset(
                                        x = item.currentOffset.x.roundToInt(),
                                        y = item.currentOffset.y.roundToInt(),
                                    )
                                },
                            )
                        }

                        if (state.isCompleted) {
                            DmtButton(
                                text = stringResource(Res.string.cogTest_hitber_next),
                                onClick = { onAction(HitberSeventhQuestionAction.OnNextClick) },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun NapkinView(
    color: NapkinColor,
    onPositioned: (Rect) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint = when (color) {
        NapkinColor.RED -> Color.Red
        NapkinColor.GREEN -> Color.Green
        NapkinColor.BLUE -> Color.Blue
        NapkinColor.YELLOW -> Color.Yellow
    }

    Image(
        painter = painterResource(Res.drawable.hitber_napkin),
        contentDescription = "${color.name} napkin",
        colorFilter = ColorFilter.tint(tint),
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(80.dp)
            .onGloballyPositioned { coords ->
                onPositioned(coords.boundsInRoot())
            },
    )
}

@Preview
@Composable
private fun HitberSeventhQuestionPreview() {
    DmtTheme {
        HitberSeventhQuestionScreen(
            state = HitberSeventhQuestionState(),
            onAction = {},
        )
    }
}
