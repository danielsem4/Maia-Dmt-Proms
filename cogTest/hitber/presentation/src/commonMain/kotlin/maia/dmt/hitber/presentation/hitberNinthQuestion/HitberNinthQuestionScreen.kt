package maia.dmt.hitber.presentation.hitberNinthQuestion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_next
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_nine_question_instruction
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_nine_question_title
import maia.dmt.core.designsystem.components.buttons.DmtButton
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.hitber.presentation.hitberNinthQuestion.components.DraggableWordCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

private const val WORD_CARD_FRACTION = 0.28f
private const val DROP_ZONE_FRACTION = 0.50f

@Composable
fun HitberNinthQuestionRoot(
    onNavigateToNextScreen: () -> Unit = {},
    viewModel: HitberNinthQuestionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HitberNinthQuestionEvent.NavigateToNextScreen -> onNavigateToNextScreen()
        }
    }

    HitberNinthQuestionScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun HitberNinthQuestionScreen(
    state: HitberNinthQuestionState,
    onAction: (HitberNinthQuestionAction) -> Unit,
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.cogTest_hitber_nine_question_title),
        onIconClick = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                // Large bold instruction
                Text(
                    text = stringResource(Res.string.cogTest_hitber_nine_question_instruction),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(DROP_ZONE_FRACTION)
                            .align(Alignment.BottomCenter),
                    ) {
                        state.dropZones.forEach { zone ->
                            val placedText = zone.placedWordId
                                ?.let { id -> state.words.find { it.id == id }?.text }
                            val isHovered = state.hoveredZoneId == zone.id
                            WordDropZone(
                                text = placedText,
                                isHovered = isHovered,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(horizontal = 2.dp)
                                    .onGloballyPositioned { coords ->
                                        onAction(
                                            HitberNinthQuestionAction.OnDropZonePositioned(
                                                zone.id,
                                                coords.boundsInRoot(),
                                            )
                                        )
                                    },
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(WORD_CARD_FRACTION)
                            .align(Alignment.TopCenter),
                    ) {
                        state.words.forEach { word ->
                            DraggableWordCard(
                                word = word,
                                onDrag = { dragAmount ->
                                    onAction(HitberNinthQuestionAction.OnWordDrag(word.id, dragAmount))
                                },
                                onDragEnd = {
                                    onAction(HitberNinthQuestionAction.OnWordDrop(word.id))
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(horizontal = 2.dp)
                                    .zIndex(if (word.isDragging) 1f else 0f)
                                    // 1. Measure the static home position BEFORE the offset is applied
                                    .onGloballyPositioned { coords ->
                                        onAction(
                                            HitberNinthQuestionAction.OnWordPositioned(
                                                word.id,
                                                coords.positionInRoot(),
                                            )
                                        )
                                    }
                                    .absoluteOffset {
                                        IntOffset(
                                            x = word.dragDelta.x.roundToInt(),
                                            y = word.dragDelta.y.roundToInt(),
                                        )
                                    },
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    DmtButton(
                        text = stringResource(Res.string.cogTest_hitber_next),
                        onClick = { onAction(HitberNinthQuestionAction.OnNextClick) },
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
            }
        },
    )
}

@Composable
private fun WordDropZone(
    text: String?,
    isHovered: Boolean,
    modifier: Modifier = Modifier,
) {
    val isOccupied = text != null
    val backgroundColor = when {
        isHovered -> MaterialTheme.colorScheme.primaryContainer
        isOccupied -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val borderColor = when {
        isHovered -> MaterialTheme.colorScheme.primary
        isOccupied -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outline
    }
    val borderWidth = if (isHovered || isOccupied) 2.5.dp else 1.5.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 12.dp),
    ) {
        if (isOccupied) {
            Text(
                text = text!!,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                color = if (isHovered)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Preview
@Composable
private fun HitberNinthQuestionPreview() {
    DmtTheme {
        HitberNinthQuestionScreen(
            state = HitberNinthQuestionState(
                words = listOf("ערב", "עם", "אכלתי", "ארוחת", "סבתא", "אתמול")
                    .mapIndexed { i, t -> WordCard(id = i, text = t) },
                dropZones = (0 until 6).map { DropZone(id = it) },
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun HitberNinthQuestionPartiallyFilledPreview() {
    DmtTheme {
        val words = listOf("ערב", "עם", "אכלתי", "ארוחת", "סבתא", "אתמול")
            .mapIndexed { i, t -> WordCard(id = i, text = t, placedInZoneId = if (i < 3) i else null) }
        val zones = (0 until 6).map { id ->
            DropZone(id = id, placedWordId = if (id < 3) id else null)
        }
        HitberNinthQuestionScreen(
            state = HitberNinthQuestionState(words = words, dropZones = zones),
            onAction = {},
        )
    }
}