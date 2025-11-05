package maia.dmt.statistics.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.feature.statistics.presentation.generated.resources.Res
import dmtproms.feature.statistics.presentation.generated.resources.statistics_trend
import dmtproms.feature.statistics.presentation.generated.resources.statistics_trend_decreasing
import dmtproms.feature.statistics.presentation.generated.resources.statistics_trend_increasing
import dmtproms.feature.statistics.presentation.generated.resources.statistics_trend_stable
import maia.dmt.core.domain.dto.ChartData
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun TrendCard(
    chartData: List<ChartData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.statistics_trend),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = calculateTrend(chartData),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
fun calculateAverage(chartData: List<ChartData>): String {
    return if (chartData.isNotEmpty()) {
        val avg = chartData.map { it.value }.average()
        val rounded = (round(avg * 10) / 10).toString()
        rounded
    } else {
        "N/A"
    }
}

@Composable
fun calculateTrend(chartData: List<ChartData>): String {
    return if (chartData.size >= 2) {
        val first = chartData.first().value
        val last = chartData.last().value
        when {
            last > first -> "↑ ${stringResource(Res.string.statistics_trend_increasing)}"
            last < first -> "↓ ${stringResource(Res.string.statistics_trend_decreasing)}"
            else -> "→ ${stringResource(Res.string.statistics_trend_stable)}"
        }
    } else {
        "N/A"
    }
}