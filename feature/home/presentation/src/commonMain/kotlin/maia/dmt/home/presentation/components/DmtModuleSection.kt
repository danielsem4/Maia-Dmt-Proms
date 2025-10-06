package maia.dmt.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.cards.DmtIconCard
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import maia.dmt.home.presentation.module.ModuleUiModel
import org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
fun DmtModuleSection(
    modifier: Modifier = Modifier,
    modules: List<ModuleUiModel> = emptyList()
) {
    val configuration = currentDeviceConfiguration()

    val columnCount = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 3
        DeviceConfiguration.MOBILE_LANDSCAPE -> 4
        DeviceConfiguration.TABLET_PORTRAIT -> 4
        DeviceConfiguration.TABLET_LANDSCAPE -> 5
        DeviceConfiguration.DESKTOP -> 6
    }

    val spacing = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 12.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 10.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 16.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 14.dp
        DeviceConfiguration.DESKTOP -> 20.dp
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                    .padding(8.dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DmtModuleSection(
                modules = listOf(
                    ModuleUiModel(
                        icon = Icons.Default.Home,
                        text = "Home",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.Settings,
                        text = "Settings",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.Notifications,
                        text = "Notifications",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.Search,
                        text = "Search",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.Favorite,
                        text = "Favorites",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.Email,
                        text = "Messages",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.ShoppingCart,
                        text = "Cart",
                        onClick = { }
                    ),
                    ModuleUiModel(
                        icon = Icons.Default.AccountCircle,
                        text = "Account",
                        onClick = { }
                    )
                )
            )
        }
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