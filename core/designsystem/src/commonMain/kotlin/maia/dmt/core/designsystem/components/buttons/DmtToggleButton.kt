package maia.dmt.core.designsystem.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ToggleOrientation {
    Row,
    Column
}

@Composable
fun DmtToggleButton(
    texts: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    orientation: ToggleOrientation,
    modifier: Modifier = Modifier,
    selectedStyle: DmtButtonStyle = DmtButtonStyle.PRIMARY,
    unselectedStyle: DmtButtonStyle = DmtButtonStyle.SECONDARY
) {
    when (orientation) {
        ToggleOrientation.Row -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                texts.forEachIndexed { index, text ->
                    val isSelected = (index == selectedIndex)
                    val style = if (isSelected) selectedStyle else unselectedStyle

                    DmtButton(
                        text = text,
                        onClick = { onSelectionChange(index) },
                        style = style,
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    )
                }
            }
        }
        ToggleOrientation.Column -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                texts.forEachIndexed { index, text ->
                    val isSelected = (index == selectedIndex)
                    val style = if (isSelected) selectedStyle else unselectedStyle

                    DmtButton(
                        text = text,
                        onClick = { onSelectionChange(index) },
                        style = style,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun DmtToggleButtonRowPreview() {
    DmtTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DmtToggleButton(
                texts = listOf("ON", "OFF"),
                selectedIndex = 0,
                onSelectionChange = {},
                orientation = ToggleOrientation.Row
            )
        }
    }
}

@Composable
@Preview
fun DmtToggleButtonColPreview() {
    DmtTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DmtToggleButton(
                texts = listOf("Option 1", "Option 2", "Option 3"),
                selectedIndex = 0,
                onSelectionChange = {},
                orientation = ToggleOrientation.Column
            )
        }
    }
}