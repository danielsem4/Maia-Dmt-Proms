package maia.dmt.activities.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.feature.activities.presentation.generated.resources.Res
import dmtproms.feature.activities.presentation.generated.resources.run_icon
import maia.dmt.activities.domain.model.ActivityItem
import maia.dmt.activities.presentation.model.ActivityUiModel
import maia.dmt.core.designsystem.components.cards.DmtIconCard
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtActivitiesSection(
    modifier: Modifier = Modifier,
    activities: List<ActivityUiModel> = emptyList()
) {

    val configuration = currentDeviceConfiguration()

    val columnCount = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 2
        DeviceConfiguration.MOBILE_LANDSCAPE -> 3
        DeviceConfiguration.TABLET_PORTRAIT -> 3
        DeviceConfiguration.TABLET_LANDSCAPE -> 4
        DeviceConfiguration.DESKTOP -> 6
    }

    val spacing = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 12.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 10.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 16.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 14.dp
        DeviceConfiguration.DESKTOP -> 20.dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (activities.isEmpty()) {
            Text(
                text = "No modules available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(activities) { activity ->
                    DmtIconCard(
                        icon = activity.icon,
                        text = activity.text,
                        onClick = activity.onClick
                    )
                }
            }
        }
    }
}

@Composable
@Preview

fun DmtActivitiesSectionPreview() {
    DmtTheme {
         DmtActivitiesSection(
             activities = listOf(
                 ActivityUiModel(
                     id = "1",
                     text = "Run",
                     icon = Res.drawable.run_icon,
                     onClick = {}
                 )
         )
         )
     }
}