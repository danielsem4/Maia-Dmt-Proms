package maia.dmt.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.memory_icon
import dmtproms.feature.home.presentation.generated.resources.no_available_modules
import maia.dmt.core.designsystem.components.cards.DmtIconCard
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.home.presentation.module.ModuleUiModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtModuleSection(
    modifier: Modifier = Modifier,
    modules: List<ModuleUiModel> = emptyList()
) {
    val configuration = currentDeviceConfiguration()

    val columnCount = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 2
        DeviceConfiguration.MOBILE_LANDSCAPE -> 2
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

    val maxVisibleRows = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 4
        DeviceConfiguration.MOBILE_LANDSCAPE -> 3
        DeviceConfiguration.TABLET_PORTRAIT -> 3
        DeviceConfiguration.TABLET_LANDSCAPE -> 3
        DeviceConfiguration.DESKTOP -> 5
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (modules.isEmpty()) {
            Text(
                text = stringResource(Res.string.no_available_modules),
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
                    .heightIn(max = calculateMaxHeight(maxVisibleRows, spacing))
                    .padding(vertical = 8.dp)
            ) {
                items(modules) { module ->
                    DmtIconCard(
                        icon = module.icon,
                        text = stringResource(module.text),
                        onClick = module.onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun calculateMaxHeight(rows: Int, spacing: androidx.compose.ui.unit.Dp): androidx.compose.ui.unit.Dp {
    val cardHeight = 90.dp
    return (cardHeight * rows) + (spacing * (rows - 1))
}

@Composable
@Preview
fun DmtModuleSectionEmptyPreview() {
    DmtTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DmtModuleSection(
                modules = emptyList()
            )
        }
    }
}