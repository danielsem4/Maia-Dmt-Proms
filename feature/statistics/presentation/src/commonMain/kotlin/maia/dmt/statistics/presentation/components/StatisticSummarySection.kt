package maia.dmt.statistics.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.feature.statistics.presentation.generated.resources.Res
import dmtproms.feature.statistics.presentation.generated.resources.statistics_average
import dmtproms.feature.statistics.presentation.generated.resources.statistics_summery
import dmtproms.feature.statistics.presentation.generated.resources.statistics_total_points
import maia.dmt.core.designsystem.components.cards.DmtTextCard
import maia.dmt.core.domain.dto.ChartData
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatisticSummarySection(
    chartData: List<ChartData>,
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
                text = stringResource(Res.string.statistics_summery),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DmtTextCard(
                    title = stringResource(Res.string.statistics_total_points),
                    value = chartData.size.toString(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )

                DmtTextCard(
                    title = stringResource(Res.string.statistics_average),
                    value = calculateAverage(chartData),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TrendCard(chartData = chartData)
        }
    }
}