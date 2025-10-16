package maia.dmt.core.designsystem.components.select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.cards.DmtCard
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

data class CheckboxOption(
    val text: String,
    val isChecked: Boolean = false
)

@Composable
fun DmtCheckboxCardGroup(
    options: List<CheckboxOption>,
    onCheckedChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    allowMultiple: Boolean = true
) {
    var selectedOptions by remember { mutableStateOf(options) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        selectedOptions.forEachIndexed { index, option ->
            DmtCard(
                text = option.text,
                onClick = {
                    selectedOptions = if (allowMultiple) {
                        // Multiple selection mode
                        selectedOptions.mapIndexed { i, opt ->
                            if (i == index) opt.copy(isChecked = !opt.isChecked)
                            else opt
                        }
                    } else {
                        // Single selection mode (radio button behavior)
                        selectedOptions.mapIndexed { i, opt ->
                            opt.copy(isChecked = i == index)
                        }
                    }

                    val checkedValues = selectedOptions
                        .filter { it.isChecked }
                        .map { it.text }
                    onCheckedChange(checkedValues)
                },
                style = if (option.isChecked) DmtCardStyle.PRIMARY else DmtCardStyle.ELEVATED,
                leadingIcon = if (option.isChecked) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checked",
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@Preview
fun CheckboxCardGroupPreview() {
    DmtTheme() {
        var selectedValues by remember { mutableStateOf<List<String>>(emptyList()) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DmtCheckboxCardGroup(
                options = listOf(
                    CheckboxOption("Great!", isChecked = true),
                    CheckboxOption("Good"),
                    CheckboxOption("In pain"),
                    CheckboxOption("Lazy"),
                    CheckboxOption("Other")
                ),
                onCheckedChange = { checkedValues ->
                    selectedValues = checkedValues
                    println("Selected: $checkedValues")
                },
                allowMultiple = true
            )
        }
    }
}

@Composable
@Preview
fun SingleSelectionCheckboxCardGroupPreview() {
    DmtTheme(darkTheme = true) {
        var selectedValue by remember { mutableStateOf<List<String>>(emptyList()) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DmtCheckboxCardGroup(
                options = listOf(
                    CheckboxOption("Great!"),
                    CheckboxOption("Good"),
                    CheckboxOption("In pain"),
                    CheckboxOption("Lazy"),
                    CheckboxOption("Other")
                ),
                onCheckedChange = { checkedValues ->
                    selectedValue = checkedValues
                    println("Selected: $checkedValues")
                },
                allowMultiple = false
            )
        }
    }
}