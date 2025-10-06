package maia.dmt.core.designsystem.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtIconCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    val configuration = currentDeviceConfiguration()

    val cardSize = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 100.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 90.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 120.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 110.dp
        DeviceConfiguration.DESKTOP -> 140.dp
    }

    val iconSize = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 40.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 36.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 48.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 44.dp
        DeviceConfiguration.DESKTOP -> 56.dp
    }

    val textStyle = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> MaterialTheme.typography.bodySmall
        DeviceConfiguration.MOBILE_LANDSCAPE -> MaterialTheme.typography.bodySmall
        DeviceConfiguration.TABLET_PORTRAIT -> MaterialTheme.typography.bodyMedium
        DeviceConfiguration.TABLET_LANDSCAPE -> MaterialTheme.typography.bodyMedium
        DeviceConfiguration.DESKTOP -> MaterialTheme.typography.bodyLarge
    }

    Card(
        modifier = modifier
            .size(cardSize)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                style = textStyle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
fun DmtIconCardPreview() {
    DmtTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            DmtIconCard(
                icon = Icons.Default.Home,
                text = "Home"
            )
        }
    }
}