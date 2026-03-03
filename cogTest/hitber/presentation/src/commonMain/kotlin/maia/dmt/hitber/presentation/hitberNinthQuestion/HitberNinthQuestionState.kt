package maia.dmt.hitber.presentation.hitberNinthQuestion

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

data class WordCard(
    val id: Int,
    val text: String,
    val homePosition: Offset = Offset.Zero,
    val dragDelta: Offset = Offset.Zero,
    val isDragging: Boolean = false,
    val placedInZoneId: Int? = null,
) {
    val currentPosition: Offset get() = homePosition + dragDelta
}

data class DropZone(
    val id: Int,
    val placedWordId: Int? = null,
    val bounds: Rect = Rect.Zero,
)

data class HitberNinthQuestionState(
    val words: List<WordCard> = emptyList(),
    val dropZones: List<DropZone> = emptyList(),
    val version: Int = 0,
    val correctAnswer: List<String> = emptyList(),
    val hoveredZoneId: Int? = null,
)
