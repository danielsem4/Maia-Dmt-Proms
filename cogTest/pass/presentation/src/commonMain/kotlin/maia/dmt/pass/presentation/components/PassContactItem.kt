package maia.dmt.pass.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PassContactItem(
    name: String,
    initial: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    initialTextColor: Color = Color.White
) {
    val deviceConfig = currentDeviceConfiguration()

    val sizing = when (deviceConfig) {
        DeviceConfiguration.MOBILE_PORTRAIT -> ContactItemSizing(
            horizontalPadding = 12.dp,
            verticalPadding = 10.dp,
            circleSize = 50.dp,
            cornerRadius = 10.dp,
            nameStyle = MaterialTheme.typography.titleSmall,
            initialStyle = MaterialTheme.typography.headlineSmall
        )
        DeviceConfiguration.MOBILE_LANDSCAPE -> ContactItemSizing(
            horizontalPadding = 14.dp,
            verticalPadding = 12.dp,
            circleSize = 55.dp,
            cornerRadius = 12.dp,
            nameStyle = MaterialTheme.typography.titleMedium,
            initialStyle = MaterialTheme.typography.headlineSmall
        )
        DeviceConfiguration.TABLET_PORTRAIT -> ContactItemSizing(
            horizontalPadding = 16.dp,
            verticalPadding = 14.dp,
            circleSize = 60.dp,
            cornerRadius = 12.dp,
            nameStyle = MaterialTheme.typography.titleMedium,
            initialStyle = MaterialTheme.typography.headlineMedium
        )
        DeviceConfiguration.TABLET_LANDSCAPE -> ContactItemSizing(
            horizontalPadding = 18.dp,
            verticalPadding = 16.dp,
            circleSize = 70.dp,
            cornerRadius = 14.dp,
            nameStyle = MaterialTheme.typography.titleLarge,
            initialStyle = MaterialTheme.typography.headlineMedium
        )
        DeviceConfiguration.DESKTOP -> ContactItemSizing(
            horizontalPadding = 20.dp,
            verticalPadding = 18.dp,
            circleSize = 80.dp,
            cornerRadius = 16.dp,
            nameStyle = MaterialTheme.typography.titleLarge,
            initialStyle = MaterialTheme.typography.headlineLarge
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(sizing.cornerRadius))
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(sizing.cornerRadius)
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = sizing.horizontalPadding,
                vertical = sizing.verticalPadding
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(sizing.circleSize)
                .background(
                    color = initialBackgroundColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                style = sizing.initialStyle,
                color = initialTextColor,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = name,
            style = sizing.nameStyle,
            color = MaterialTheme.colorScheme.extended.textPrimary
        )
    }
}

private data class ContactItemSizing(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val circleSize: Dp,
    val cornerRadius: Dp,
    val nameStyle: TextStyle,
    val initialStyle: TextStyle
)

@Composable
@Preview
fun PassContactItemPreview() {
    DmtTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PassContactItem(
                name = "אבנר גבני",
                initial = "א",
                onClick = {}
            )

            PassContactItem(
                name = "חנה כהן",
                initial = "ח",
                onClick = {},
            )

            PassContactItem(
                name = "David Levi",
                initial = "D",
                onClick = {},
            )
        }
    }
}