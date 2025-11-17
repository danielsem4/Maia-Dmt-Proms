package maia.dmt.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class DmtButtonStyle {
    PRIMARY,
    DESTRUCTIVE_PRIMARY,
    SECONDARY,
    DESTRUCTIVE_SECONDARY,
    TEXT
}

@Composable
fun DmtButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: DmtButtonStyle = DmtButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val deviceConfig = currentDeviceConfiguration()

    val buttonSizing = when(deviceConfig) {
        DeviceConfiguration.MOBILE_PORTRAIT -> ButtonSizing(
            minWidth = 120.dp,
            maxWidth = 280.dp,
            verticalPadding = 6.dp,
            horizontalPadding = 6.dp,
            iconSpacing = 8.dp,
            iconSize = 20.dp,
            textSize = 14.sp
        )
        DeviceConfiguration.MOBILE_LANDSCAPE -> ButtonSizing(
            minWidth = 140.dp,
            maxWidth = 320.dp,
            verticalPadding = 8.dp,
            horizontalPadding = 8.dp,
            iconSpacing = 10.dp,
            iconSize = 22.dp,
            textSize = 15.sp
        )
        DeviceConfiguration.TABLET_PORTRAIT -> ButtonSizing(
            minWidth = 160.dp,
            maxWidth = 360.dp,
            verticalPadding = 10.dp,
            horizontalPadding = 12.dp,
            iconSpacing = 12.dp,
            iconSize = 24.dp,
            textSize = 20.sp
        )
        DeviceConfiguration.TABLET_LANDSCAPE -> ButtonSizing(
            minWidth = 180.dp,
            maxWidth = 400.dp,
            verticalPadding = 12.dp,
            horizontalPadding = 14.dp,
            iconSpacing = 12.dp,
            iconSize = 26.dp,
            textSize = 20.sp
        )
        DeviceConfiguration.DESKTOP -> ButtonSizing(
            minWidth = 200.dp,
            maxWidth = 450.dp,
            verticalPadding = 14.dp,
            horizontalPadding = 16.dp,
            iconSpacing = 14.dp,
            iconSize = 28.dp,
            textSize = 24.sp
        )
    }

    val colors = when(style) {
        DmtButtonStyle.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtButtonStyle.DESTRUCTIVE_PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtButtonStyle.DESTRUCTIVE_SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        DmtButtonStyle.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    val defaultBorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.extended.disabledOutline
    )
    val border = when {
        style == DmtButtonStyle.PRIMARY && !enabled -> defaultBorderStroke
        style == DmtButtonStyle.SECONDARY -> defaultBorderStroke
        style == DmtButtonStyle.DESTRUCTIVE_PRIMARY && !enabled -> defaultBorderStroke
        style == DmtButtonStyle.DESTRUCTIVE_SECONDARY -> {
            val borderColor = if(enabled) {
                MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
            } else {
                MaterialTheme.colorScheme.extended.disabledOutline
            }
            BorderStroke(
                width = 2.dp,
                color = borderColor
            )
        }
        else -> null
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .widthIn(min = buttonSizing.minWidth, max = buttonSizing.maxWidth),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = colors,
        border = border
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(
                    vertical = buttonSizing.verticalPadding,
                    horizontal = buttonSizing.horizontalPadding
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(
                        alpha = if(isLoading) 1f else 0f
                    ),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    buttonSizing.iconSpacing,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if(isLoading) 0f else 1f
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = buttonSizing.textSize
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class ButtonSizing(
    val minWidth: androidx.compose.ui.unit.Dp,
    val maxWidth: androidx.compose.ui.unit.Dp,
    val verticalPadding: androidx.compose.ui.unit.Dp,
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val iconSpacing: androidx.compose.ui.unit.Dp,
    val iconSize: androidx.compose.ui.unit.Dp,
    val textSize: androidx.compose.ui.unit.TextUnit
)

@Composable
@Preview
fun DmtPrimaryButtonPreview() {
    DmtTheme(
        darkTheme = true
    ) {
        DmtButton(
            text = "Hello world!",
            onClick = {},
            style = DmtButtonStyle.PRIMARY,
        )
    }
}

@Composable
@Preview
fun DmtSecondaryButtonPreview() {
    DmtTheme(
        darkTheme = true
    ) {
        DmtButton(
            text = "Hello world!",
            onClick = {},
            style = DmtButtonStyle.SECONDARY
        )
    }
}

@Composable
@Preview
fun DmtDestructivePrimaryButtonPreview() {
    DmtTheme(
        darkTheme = true
    ) {
        DmtButton(
            text = "Hello world!",
            onClick = {},
            style = DmtButtonStyle.DESTRUCTIVE_PRIMARY
        )
    }
}

@Composable
@Preview
fun DmtDestructiveSecondaryButtonPreview() {
    DmtTheme(
        darkTheme = true
    ) {
        DmtButton(
            text = "Hello world!",
            onClick = {},
            style = DmtButtonStyle.DESTRUCTIVE_SECONDARY
        )
    }
}

@Composable
@Preview
fun DmtTextButtonPreview() {
    DmtTheme(
        darkTheme = true
    ) {
        DmtButton(
            text = "Hello world!",
            onClick = {},
            style = DmtButtonStyle.TEXT
        )
    }
}

@Composable
@Preview
fun DmtPrimaryButtonIconPreview() {
    DmtTheme {
        DmtButton(
            text = "Hello world!",
            onClick = {},
            style = DmtButtonStyle.SECONDARY,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
    }
}

@Composable
@Preview
fun DmtPrimaryButtonLongTextPreview() {
    DmtTheme(
        darkTheme = true
    ) {
        DmtButton(
            text = "Date & Time",
            onClick = {},
            style = DmtButtonStyle.PRIMARY,
        )
    }
}