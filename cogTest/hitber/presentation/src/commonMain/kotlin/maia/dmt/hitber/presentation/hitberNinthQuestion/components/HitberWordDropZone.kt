package maia.dmt.hitber.presentation.hitberNinthQuestion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HitberWordDropZone(
    text: String?,
    isHovered: Boolean,
    modifier: Modifier = Modifier,
) {
    val isOccupied = text != null
    val shape = RoundedCornerShape(8.dp)

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
            .background(color = backgroundColor, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
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
