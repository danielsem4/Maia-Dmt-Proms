package maia.dmt.core.designsystem.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class DmtCardStyle {
    PRIMARY,
    SECONDARY,
    OUTLINED,
    ELEVATED
}

@Composable
fun DmtCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: DmtCardStyle = DmtCardStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = if (!enabled) {
        // When disabled, use consistent styling across all card types
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.extended.disabledFill,
            contentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    } else {
        when(style) {
            DmtCardStyle.PRIMARY -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
            DmtCardStyle.SECONDARY -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DmtCardStyle.OUTLINED -> CardDefaults.cardColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.extended.textSecondary
            )
            DmtCardStyle.ELEVATED -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    val defaultDisabledBorder = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.extended.disabledOutline
    )

    val border = when {
        // When disabled, always show border like the button
        !enabled -> defaultDisabledBorder
        style == DmtCardStyle.OUTLINED -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
        style == DmtCardStyle.ELEVATED -> BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        else -> null
    }

    val elevation = when {
        !enabled -> CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
        style == DmtCardStyle.ELEVATED -> CardDefaults.cardElevation(
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
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        border = border,
        elevation = elevation
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .alpha(
                        alpha = if(isLoading) 1f else 0f
                    ),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp,
                    Alignment.Start
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if(isLoading) 0f else 1f
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
@Preview
fun DmtPrimaryCardPreview() {
    DmtTheme(darkTheme = true) {
        DmtCard(
            text = "Primary Card",
            onClick = {},
            style = DmtCardStyle.PRIMARY,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

@Composable
@Preview
fun DmtSecondaryCardPreview() {
    DmtTheme(darkTheme = true) {
        DmtCard(
            text = "Secondary Card",
            onClick = {},
            style = DmtCardStyle.SECONDARY,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

@Composable
@Preview
fun DmtOutlinedCardPreview() {
    DmtTheme(darkTheme = true) {
        DmtCard(
            text = "Outlined Card",
            onClick = {},
            style = DmtCardStyle.OUTLINED,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

@Composable
@Preview
fun DmtElevatedCardPreview() {
    DmtTheme(darkTheme = false) {
        DmtCard(
            text = "Elevated Card",
            onClick = {},
            style = DmtCardStyle.ELEVATED,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

@Composable
@Preview
fun DmtCardLoadingPreview() {
    DmtTheme(darkTheme = true) {
        DmtCard(
            text = "Loading Card",
            onClick = {},
            style = DmtCardStyle.PRIMARY,
            isLoading = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

@Composable
@Preview
fun DmtCardDisabledPreview() {
    DmtTheme(darkTheme = true) {
        DmtCard(
            text = "Disabled Card",
            onClick = {},
            style = DmtCardStyle.PRIMARY,
            enabled = false,
        )
    }
}