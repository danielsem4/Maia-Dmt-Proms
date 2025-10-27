package maia.dmt.statistics.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtEvaluationStatisticsCard(
    name: String,
    timesDone: Int,
    lastDateDone: String,
    style: DmtCardStyle = DmtCardStyle.ELEVATED,
    isClickable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = when(style) {
        DmtCardStyle.PRIMARY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtCardStyle.SECONDARY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtCardStyle.OUTLINED -> CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtCardStyle.ELEVATED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    val border = when {
        style == DmtCardStyle.OUTLINED -> BorderStroke(
            width = 1.dp,
            color = if(isClickable) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.extended.disabledOutline
            }
        )
        style == DmtCardStyle.ELEVATED -> BorderStroke(
            width = 1.dp,
            color = if(isClickable) {
                MaterialTheme.colorScheme.outlineVariant
            } else {
                MaterialTheme.colorScheme.extended.disabledOutline
            }
        )
        !isClickable -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.extended.disabledOutline
        )
        else -> null
    }

    val elevation = when(style) {
        DmtCardStyle.ELEVATED -> CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp,
            disabledElevation = 0.dp
        )
        else -> CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
            disabledElevation = 0.dp
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(enabled = isClickable, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        border = border,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = if (isClickable) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textDisabled
                }
            )

            Text(
                text = timesDone.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isClickable) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.extended.textDisabled
                },
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = lastDateDone,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isClickable) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.extended.textDisabled
                },
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
@Preview
fun DmtEvaluationStatisticsCardPreview() {
    DmtTheme {
         DmtEvaluationStatisticsCard(
             name = "Blood Pressure",
             timesDone = 1,
             lastDateDone = "Today",
             isClickable = true,
             onClick = {}
         )
     }
}