package maia.dmt.core.designsystem.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtParagraphCard(
    modifier: Modifier = Modifier,
    text: String = "",
    isLoading: Boolean = false,
    style: DmtCardStyle = DmtCardStyle.ELEVATED
) {
    val colors = when(style) {
        DmtCardStyle.PRIMARY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
        DmtCardStyle.SECONDARY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DmtCardStyle.OUTLINED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
        DmtCardStyle.ELEVATED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    val border = when(style) {
        DmtCardStyle.OUTLINED -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
        DmtCardStyle.ELEVATED -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        else -> null
    }

    val elevation = when(style) {
        DmtCardStyle.ELEVATED -> CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
        else -> CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        border = border,
        elevation = elevation
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .alpha(
                        alpha = if(isLoading) 1f else 0f
                    ),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(
                        alpha = if(isLoading) 0f else 1f
                    )
            )
        }
    }
}

@Composable
@Preview
fun DmtParagraphCardPreview() {
    DmtTheme(darkTheme = true) {
        DmtParagraphCard(
            text = "This is a paragraph card with centered text. It can contain multiple lines of text that will wrap naturally and remain centered."
        )
    }
}

@Composable
@Preview
fun DmtParagraphCardLoadingPreview() {
    DmtTheme(darkTheme = true) {
        DmtParagraphCard(
            text = "Loading...",
            isLoading = true
        )
    }
}

@Composable
@Preview
fun DmtParagraphCardPrimaryPreview() {
    DmtTheme(darkTheme = false) {
        DmtParagraphCard(
            text = "This is a primary style paragraph card with centered text.",
            style = DmtCardStyle.PRIMARY
        )
    }
}