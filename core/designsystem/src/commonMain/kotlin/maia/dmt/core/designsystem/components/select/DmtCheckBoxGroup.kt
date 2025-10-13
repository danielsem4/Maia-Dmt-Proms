package maia.dmt.core.designsystem.components.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtCheckBoxGroup(
    items: List<String>,
    selectedItems: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedItems.contains(item),
                    onCheckedChange = { checked ->
                        val newSelection = if (checked) {
                            selectedItems + item
                        } else {
                            selectedItems - item
                        }
                        onSelectionChange(newSelection)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
@Preview

fun DmtCheckBoxGroupPreview() {
    DmtTheme {
        DmtCheckBoxGroup(
            items = listOf("Item 1", "Item 2", "Item 3"),
            selectedItems = listOf(),
            onSelectionChange = {}
        )
    }
}