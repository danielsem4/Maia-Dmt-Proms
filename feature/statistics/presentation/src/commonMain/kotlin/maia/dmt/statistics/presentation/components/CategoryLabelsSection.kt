package maia.dmt.statistics.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.feature.statistics.presentation.generated.resources.Res
import dmtproms.feature.statistics.presentation.generated.resources.statistics_category_labels
import org.jetbrains.compose.resources.stringResource

@Composable
fun CategoryLabelsSection(
    categoryLabels: Map<Float, String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.statistics_category_labels),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            categoryLabels.forEach { (value, label) ->
                CategoryLabelItem(
                    label = label,
                    value = value.toInt().toString(),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}