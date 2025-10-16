package maia.dmt.core.designsystem.components.scale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtScaleSlider(
    startValue: Int,
    endValue: Int,
    step: Int = 1,
    startText: String,
    endText: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: Int? = null
) {
    var currentValue by remember {
        mutableStateOf(initialValue ?: startValue)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Slider(
            value = currentValue.toFloat(),
            onValueChange = { newValue ->
                currentValue = newValue.toInt()
                onValueChange(currentValue)
            },
            valueRange = startValue.toFloat()..endValue.toFloat(),
            steps = ((endValue - startValue) / step) - 1,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                androidx.compose.material3.Text(
                    text = startValue.toString(),
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
                androidx.compose.material3.Text(
                    text = startText,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }

            androidx.compose.material3.Text(
                text = currentValue.toString(),
                style = androidx.compose.material3.MaterialTheme.typography.displayMedium
            )

            Column(
                horizontalAlignment = Alignment.End
            ) {
                androidx.compose.material3.Text(
                    text = endValue.toString(),
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
                androidx.compose.material3.Text(
                    text = endText,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
@Preview
fun DmtScaleSliderPreview() {
    DmtTheme {
        var painValue by remember { mutableStateOf(6) }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DmtScaleSlider(
                startValue = 0,
                endValue = 10,
                step = 1,
                startText = "not in pain",
                endText = "in pain",
                initialValue = 6,
                onValueChange = { value ->
                    painValue = value
                    println("Pain level: $value")
                }
            )
        }
    }
}