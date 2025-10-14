package maia.dmt.core.designsystem.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dmtproms.core.designsystem.generated.resources.Res
import dmtproms.core.designsystem.generated.resources.arrow_left_icon
import dmtproms.core.designsystem.generated.resources.log_out_icon
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtBaseScreen(
    modifier: Modifier = Modifier,
    titleText: String = "Screen",
    iconBar: ImageVector? = vectorResource(Res.drawable.arrow_left_icon),
    onIconClick: (() -> Unit)? = null,
    textBar: String? = null,
//    screenOrientation: ScreenOrientation = ScreenOrientation.UNSPECIFIED,
    content: @Composable () -> Unit,
) {
    val configuration = currentDeviceConfiguration()

    val windowInsets = WindowInsets.statusBars
    val density = LocalDensity.current
    val statusBarHeight = with(density) { windowInsets.getTop(density).toDp() }
    val hasStatusBarInsets = statusBarHeight > 0.dp

    val iconSize = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 24.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 22.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 28.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 26.dp
        DeviceConfiguration.DESKTOP -> 32.dp
    }

    val titleTextStyle = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> MaterialTheme.typography.titleLarge
        DeviceConfiguration.MOBILE_LANDSCAPE -> MaterialTheme.typography.titleMedium
        DeviceConfiguration.TABLET_PORTRAIT -> MaterialTheme.typography.titleLarge
        DeviceConfiguration.TABLET_LANDSCAPE -> MaterialTheme.typography.titleLarge
        DeviceConfiguration.DESKTOP -> MaterialTheme.typography.headlineMedium
    }

    val barTextStyle = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> MaterialTheme.typography.bodyMedium
        DeviceConfiguration.MOBILE_LANDSCAPE -> MaterialTheme.typography.bodySmall
        DeviceConfiguration.TABLET_PORTRAIT -> MaterialTheme.typography.bodyLarge
        DeviceConfiguration.TABLET_LANDSCAPE -> MaterialTheme.typography.bodyMedium
        DeviceConfiguration.DESKTOP -> MaterialTheme.typography.titleMedium
    }

    val horizontalPadding = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 8.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 12.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 12.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 14.dp
        DeviceConfiguration.DESKTOP -> 32.dp
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            val topBarModifier = if (hasStatusBarInsets) {
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
            } else {
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            }

            Column(modifier = topBarModifier) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = MaterialTheme.colorScheme.primary,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = horizontalPadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left icon
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (iconBar != null) {
                                IconButton(
                                    onClick = { onIconClick?.invoke() }
                                ) {
                                    Icon(
                                        imageVector = iconBar,
                                        contentDescription = "Navigation icon",
                                        modifier = Modifier.size(iconSize),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Center title
                        Box(
                            modifier = Modifier.weight(2f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = titleText,
                                style = titleTextStyle,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }

                        // Right text
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (textBar != null) {
                                Text(
                                    text = textBar,
                                    style = barTextStyle,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.End,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}


@Composable
@Preview
fun DmtBaseScreenPreview() {
    DmtTheme {
        DmtBaseScreen(
            titleText = "Home Screen",
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Content goes here")
            }
        }
    }
}

@Composable
@Preview
fun DmtBaseScreenWithIconPreview() {
    DmtTheme {
        DmtBaseScreen(
            titleText = "Home Screen",
            iconBar = vectorResource(Res.drawable.log_out_icon),

        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Content goes here")
            }
        }
    }
}