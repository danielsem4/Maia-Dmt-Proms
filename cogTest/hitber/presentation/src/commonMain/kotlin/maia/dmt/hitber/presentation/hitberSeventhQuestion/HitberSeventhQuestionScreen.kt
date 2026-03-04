package maia.dmt.hitber.presentation.hitberSeventhQuestion

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import maia.dmt.core.designsystem.components.animations.AnimatedSpeaker
import maia.dmt.core.designsystem.components.dialogs.DmtTransparentDialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_drag_and_drop_title
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_listen
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_fridge
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_open_fridge
import dmtproms.cogtest.hitber.presentation.generated.resources.hitber_speaker
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.core.presentation.util.sound.rememberSoundPlayer
import maia.dmt.hitber.presentation.hitberSeventhQuestion.components.DraggableItem
import maia.dmt.hitber.presentation.hitberSeventhQuestion.components.HitberTableWithNapkins
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
    val soundPlayer = rememberSoundPlayer(
        onCompletion = { viewModel.onAction(HitberSeventhQuestionAction.OnAudioComplete) }
    )

    LaunchedEffect(state.isPlayingAudio) {
        if (state.isPlayingAudio) {
            soundPlayer.play(state.instructionUrl)
        } else {
            soundPlayer.stop()
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
    )
}

@Composable
fun HitberSeventhQuestionScreen(
    state: HitberSeventhQuestionState,
    onAction: (HitberSeventhQuestionAction) -> Unit,
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_drag_and_drop_title),
        onIconClick = {},
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
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
                        onClick = { onAction(HitberSeventhQuestionAction.OnListenClick) },
                        enabled = !state.isPlayingAudio,
                    )
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
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

                            val fridgeWidthPx = maxWidthPx * 0.3f
                            val fridgeHeightPx = maxHeightPx

                            LaunchedEffect(maxWidthPx, maxHeightPx) {
                                val layoutMap = calculateFridgeLayout(fridgeWidthPx, fridgeHeightPx)

                                onAction(
                                    HitberSeventhQuestionAction.OnLayoutReady(
                                        fridgeWidthPx = fridgeWidthPx,
                                        screenHeightPx = maxHeightPx,
                                        initialPositions = layoutMap
                                    )
                                )
                            }

                            Row(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(0.3f)
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
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.weight(0.35f))

                                HitberTableWithNapkins(
                                    modifier = Modifier
                                        .weight(0.35f)
                                        .fillMaxHeight(),
                                    onNapkinPositioned = { color, rect ->
                                        onAction(
                                            HitberSeventhQuestionAction.OnNapkinPositioned(
                                                color,
                                                rect
                                            )
                                        )
                                    },
                                )
                            }

                            Box(modifier = Modifier.matchParentSize()) {
                                state.items.forEach { item ->
                                    val itemSizeDp = with(density) {
                                        (fridgeWidthPx * item.type.relativeSize()).toDp()
                                    }

                                    DraggableItem(
                                        item = item,
                                        isFridgeOpen = state.isFridgeOpen,
                                        onDrag = { dragAmount ->
                                            onAction(
                                                HitberSeventhQuestionAction.OnItemDrag(
                                                    item.id,
                                                    dragAmount
                                                )
                                            )
                                        },
                                        modifier = Modifier
                                            .offset {
                                                IntOffset(
                                                    x = item.currentOffset.x.roundToInt(),
                                                    y = item.currentOffset.y.roundToInt(),
                                                )
                                            }
                                            .size(itemSizeDp)
                                    )
                                }
                            }
                        }
                    }
                }

                DmtButton(
                    text = stringResource(Res.string.cogTest_hitber_next),
                    onClick = { onAction(HitberSeventhQuestionAction.OnNextClick) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(12.dp),
                )
            }

            if (state.isPlayingAudio) {
                DmtTransparentDialog(dismissOnScrimClick = false) {
                    AnimatedSpeaker(modifier = Modifier.size(150.dp))
                }
            }
        }
    )
}


/**
 * Maps each ItemType to a specific coordinate on the fridge image shelves.
 * Coordinates are relative to the fridge container (0,0 is top-left of fridge area).
 */
private fun calculateFridgeLayout(fridgeW: Float, fridgeH: Float): Map<FridgeItemType, Offset> {
    val leftX = fridgeW * 0.16f
    val centerX = fridgeW * 0.35f
    val rightX = fridgeW * 0.45f
    val doorX = fridgeW * 0.72f

    val topShelfY = fridgeH * 0.18f
    val middleShelfTopY = fridgeH * 0.36f
    val middleShelfBottomY = fridgeH * 0.52f
    val bottomShelfY = fridgeH * 0.68f

    val doorTopY = fridgeH * 0.55f
    val doorBottomY = fridgeH * 0.75f

    return mapOf(
        FridgeItemType.GIL to Offset(leftX, middleShelfTopY),
        FridgeItemType.KOTEG to Offset(rightX, middleShelfTopY),

        FridgeItemType.GRAPE to Offset(leftX, middleShelfBottomY),
        FridgeItemType.CAN to Offset(rightX, middleShelfBottomY),

        FridgeItemType.CHICKEN to Offset(centerX, bottomShelfY),

        FridgeItemType.MILK to Offset(doorX, doorTopY),
        FridgeItemType.JUICE to Offset(doorX, doorBottomY)
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