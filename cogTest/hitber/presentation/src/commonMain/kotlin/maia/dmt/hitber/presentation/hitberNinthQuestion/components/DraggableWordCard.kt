package maia.dmt.hitber.presentation.hitberNinthQuestion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.hitber.presentation.hitberNinthQuestion.WordCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DraggableWordCard(
    word: WordCard,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPlacedElsewhere = word.placedInZoneId != null && !word.isDragging
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .alpha(if (isPlacedElsewhere) 0.4f else 1f)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .pointerInput(word.id) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = onDragEnd,
                    onDragCancel = onDragEnd,
                )
            },
    ) {
        Text(
            text = word.text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

@Preview
@Composable
private fun DraggableWordCardPreview() {
    DmtTheme {
        DraggableWordCard(
            word = WordCard(id = 0, text = "אתמול"),
            onDrag = {},
            onDragEnd = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun DraggableWordCardPlacedPreview() {
    DmtTheme {
        DraggableWordCard(
            word = WordCard(id = 0, text = "אתמול", placedInZoneId = 2),
            onDrag = {},
            onDragEnd = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}