package maia.dmt.core.designsystem.components.toast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.upload_icon
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ToastType {
    Normal, Success, Info, Warning, Error
}


data class ToastDimensions(
    val iconSize: Dp,
    val iconPadding: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val fontSize: TextUnit,
    val cornerRadius: Dp,
    val minWidth: Dp,
    val maxWidth: Dp?,
    val spacerWidth: Dp,
    val isVertical: Boolean = false
)

@Composable
fun DmtToastView(
    message: String,
    type: ToastType = ToastType.Normal,
    backgroundColor: Color? = null,
    deviceConfiguration: DeviceConfiguration = currentDeviceConfiguration()
) {
    val resolvedColor = backgroundColor ?: when (type) {
        ToastType.Normal -> Color.DarkGray
        ToastType.Success -> Color(0xFF4CAF50)
        ToastType.Info -> Color(0xFF2196F3)
        ToastType.Warning -> Color(0xFFFFC107)
        ToastType.Error -> Color(0xFFF44336)
    }

    val resolvedIcon = when (type) {
        ToastType.Success -> Res.drawable.upload_icon
        ToastType.Info -> Res.drawable.upload_icon
        ToastType.Warning -> Res.drawable.upload_icon
        ToastType.Error -> Res.drawable.upload_icon
        else -> Res.drawable.upload_icon
    }

    val dimensions = getToastDimensions(deviceConfiguration)

    Surface(
        shape = RoundedCornerShape(dimensions.cornerRadius),
        color = resolvedColor,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
            .defaultMinSize(minWidth = dimensions.minWidth)
            .then(
                if (dimensions.maxWidth != null) {
                    Modifier.wrapContentWidth(unbounded = false)
                        .fillMaxWidth(fraction = 0.9f)
                        .defaultMinSize(minWidth = dimensions.minWidth)
                } else {
                    Modifier.wrapContentWidth()
                }
            )
    ) {
        if (dimensions.isVertical) {
            // Vertical layout for very small screens
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(
                    horizontal = dimensions.horizontalPadding,
                    vertical = dimensions.verticalPadding
                )
            ) {
                Icon(
                    painter = painterResource(resolvedIcon),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .size(dimensions.iconSize)
                        .padding(dimensions.iconPadding)
                )

                Text(
                    text = message,
                    color = White,
                    fontSize = dimensions.fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            // Horizontal layout for normal screens
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    horizontal = dimensions.horizontalPadding,
                    vertical = dimensions.verticalPadding
                )
            ) {
                Icon(
                    painter = painterResource(resolvedIcon),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .size(dimensions.iconSize)
                        .padding(dimensions.iconPadding)
                )

                Spacer(modifier = Modifier.width(dimensions.spacerWidth))

                Text(
                    text = message,
                    color = White,
                    fontSize = dimensions.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Returns appropriate dimensions based on device configuration
 */
private fun getToastDimensions(deviceConfiguration: DeviceConfiguration): ToastDimensions {
    return when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> ToastDimensions(
            iconSize = 40.dp,
            iconPadding = 4.dp,
            horizontalPadding = 12.dp,
            verticalPadding = 10.dp,
            fontSize = 14.sp,
            cornerRadius = 24.dp,
            minWidth = 100.dp,
            maxWidth = 280.dp,
            spacerWidth = 8.dp,
            isVertical = false
        )

        DeviceConfiguration.MOBILE_LANDSCAPE -> ToastDimensions(
            iconSize = 36.dp,
            iconPadding = 4.dp,
            horizontalPadding = 14.dp,
            verticalPadding = 8.dp,
            fontSize = 14.sp,
            cornerRadius = 20.dp,
            minWidth = 120.dp,
            maxWidth = 400.dp,
            spacerWidth = 10.dp,
            isVertical = false
        )

        DeviceConfiguration.TABLET_PORTRAIT -> ToastDimensions(
            iconSize = 48.dp,
            iconPadding = 5.dp,
            horizontalPadding = 16.dp,
            verticalPadding = 12.dp,
            fontSize = 16.sp,
            cornerRadius = 32.dp,
            minWidth = 140.dp,
            maxWidth = 420.dp,
            spacerWidth = 12.dp,
            isVertical = false
        )

        DeviceConfiguration.TABLET_LANDSCAPE -> ToastDimensions(
            iconSize = 52.dp,
            iconPadding = 5.dp,
            horizontalPadding = 18.dp,
            verticalPadding = 12.dp,
            fontSize = 18.sp,
            cornerRadius = 40.dp,
            minWidth = 160.dp,
            maxWidth = 500.dp,
            spacerWidth = 14.dp,
            isVertical = false
        )

        DeviceConfiguration.DESKTOP -> ToastDimensions(
            iconSize = 60.dp,
            iconPadding = 6.dp,
            horizontalPadding = 16.dp,
            verticalPadding = 12.dp,
            fontSize = 20.sp,
            cornerRadius = 56.dp,
            minWidth = 120.dp,
            maxWidth = null,
            spacerWidth = 12.dp,
            isVertical = false
        )
    }
}

// Preview composables for different screen sizes
@Composable
@Preview
fun DmtToastViewPreviewMobilePortrait() {
    DmtTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DmtToastView(
                message = "Short message",
                type = ToastType.Success,
                deviceConfiguration = DeviceConfiguration.MOBILE_PORTRAIT
            )
            DmtToastView(
                message = "This is a longer toast message that might need to wrap",
                type = ToastType.Error,
                deviceConfiguration = DeviceConfiguration.MOBILE_PORTRAIT
            )
        }
    }
}

@Composable
@Preview
fun DmtToastViewPreviewTabletPortrait() {
    DmtTheme {
        DmtToastView(
            message = "Tablet portrait toast message",
            type = ToastType.Info,
            deviceConfiguration = DeviceConfiguration.TABLET_PORTRAIT
        )
    }
}

@Composable
@Preview
fun DmtToastViewPreviewTabletLandscape() {
    DmtTheme {
        DmtToastView(
            message = "Tablet landscape toast with more content",
            type = ToastType.Warning,
            deviceConfiguration = DeviceConfiguration.TABLET_LANDSCAPE
        )
    }
}

@Composable
@Preview
fun DmtToastViewPreviewDesktop() {
    DmtTheme {
        DmtToastView(
            message = "Desktop version with original styling",
            type = ToastType.Success,
            deviceConfiguration = DeviceConfiguration.DESKTOP
        )
    }
}