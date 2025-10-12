package maia.dmt.core.designsystem.components.select

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtRadioButtonGroup(
    items: List<String>,
    selectedItem: String?,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.selectableGroup()) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (item == selectedItem),
                        onClick = { onSelectionChange(item) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (item == selectedItem),
                    onClick = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
@Preview
fun DmtRadioButtonGroupPreview() {
    DmtTheme {
         DmtRadioButtonGroup(
             items = listOf("Item 1", "Item 2", "Item 3"),
             selectedItem = "Item 1",
             onSelectionChange = {}
         )
     }
}