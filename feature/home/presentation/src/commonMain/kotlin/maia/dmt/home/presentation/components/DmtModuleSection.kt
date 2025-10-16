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
import maia.dmt.core.designsystem.components.cards.DmtIconCard
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.home.presentation.module.ModuleUiModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.ceil

@Composable
fun DmtModuleSection(
    modifier: Modifier = Modifier,
    modules: List<ModuleUiModel> = emptyList()
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

    val maxVisibleRows = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 3
        DeviceConfiguration.MOBILE_LANDSCAPE -> 2
        DeviceConfiguration.TABLET_PORTRAIT -> 3
        DeviceConfiguration.TABLET_LANDSCAPE -> 3
        DeviceConfiguration.DESKTOP -> 5
    }

    val totalRows = ceil(modules.size.toFloat() / columnCount).toInt()
    val shouldConstrainHeight = totalRows > maxVisibleRows

    val estimatedCardHeight = 100.dp

    val maxHeight = if (shouldConstrainHeight) {
        (estimatedCardHeight * maxVisibleRows) + (spacing * (maxVisibleRows - 1))
    } else {
        null
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (modules.isEmpty()) {
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
                    .then(
                        if (maxHeight != null) {
                            Modifier.height(maxHeight)
                        } else {
                            Modifier
                        }
                    )
                    .padding(vertical = 8.dp)
            ) {
                items(modules) { module ->
                    DmtIconCard(
                        icon = module.icon,
                        text = module.text,
                        onClick = module.onClick
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun DmtModuleSectionPreview() {
    DmtTheme {
        DmtModuleSection(
            modules = listOf(
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Home",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Profile",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Settings",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Notifications",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Search",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Favorites",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Messages",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Cart",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Account",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "History",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "Support",
                    onClick = { }
                ),
                ModuleUiModel(
                    icon = Res.drawable.memory_icon,
                    text = "About",
                    onClick = { }
                )
            )
        )
    }
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