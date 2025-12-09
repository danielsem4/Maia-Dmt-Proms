package maia.dmt.core.designsystem.components.select

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun <T> DmtDropDown(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Select an option",
    leadingIcon: ImageVector? = null,
    itemContent: @Composable (T) -> Unit,
    selectedItemContent: @Composable (T) -> Unit = itemContent,
    dropdownWidth: Dp? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (enabled) containerColor else containerColor.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .border(
                    width = borderWidth,
                    color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clickable(enabled = enabled) { expanded = !expanded }
                .padding(contentPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (enabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                if (selectedItem != null) {
                    selectedItemContent(selectedItem)
                } else {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(if (expanded) 180f else 0f),
                tint = if (enabled) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = dropdownWidth?.let { Modifier.width(it) } ?: Modifier,
            offset = DpOffset(0.dp, 4.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { itemContent(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private data class PreviewItem(
    val id: Int,
    val name: String,
    val description: String? = null
)

@Composable
@Preview
fun DmtDropDownPreview() {
    DmtTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var selectedSimple by remember { mutableStateOf<String?>(null) }
            DmtDropDown(
                items = listOf("Option 1", "Option 2", "Option 3"),
                selectedItem = selectedSimple,
                onItemSelected = { selectedSimple = it },
                placeholder = "Select an option",
                itemContent = { Text(it) }
            )

            var selectedWithIcon by remember { mutableStateOf<String?>(null) }
            DmtDropDown(
                items = listOf("Red", "Green", "Blue"),
                selectedItem = selectedWithIcon,
                onItemSelected = { selectedWithIcon = it },
                placeholder = "Select a color",
                leadingIcon = Icons.Default.ArrowDropDown,
                itemContent = { Text(it) }
            )

            var selectedComplex by remember { mutableStateOf<PreviewItem?>(null) }
            val complexItems = listOf(
                PreviewItem(1, "Item 1", "Description 1"),
                PreviewItem(2, "Item 2", "Description 2"),
                PreviewItem(3, "Item 3", "Description 3")
            )

            DmtDropDown(
                items = complexItems,
                selectedItem = selectedComplex,
                onItemSelected = { selectedComplex = it },
                placeholder = "Select an item",
                itemContent = { item ->
                    Column {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        item.description?.let { desc ->
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                },
                selectedItemContent = { item ->
                    Text(text = item.name)
                }
            )

            DmtDropDown(
                items = listOf("Disabled 1", "Disabled 2"),
                selectedItem = "Disabled 1",
                onItemSelected = {},
                enabled = false,
                itemContent = { Text(it) }
            )

            var selectedCustom by remember { mutableStateOf<String?>(null) }
            DmtDropDown(
                items = listOf("Custom 1", "Custom 2", "Custom 3"),
                selectedItem = selectedCustom,
                onItemSelected = { selectedCustom = it },
                placeholder = "Custom styled",
                cornerRadius = 16.dp,
                borderWidth = 2.dp,
                borderColor = MaterialTheme.colorScheme.primary,
                contentPadding = PaddingValues(20.dp),
                itemContent = { Text(it) }
            )
        }
    }
}