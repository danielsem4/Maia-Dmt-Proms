package maia.dmt.medication.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dmtproms.feature.medication.presentation.generated.resources.Res
import dmtproms.feature.medication.presentation.generated.resources.medications_icon
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.medication.presentation.model.ReportedMedicationUiModel
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtMedicationLogItem(
    medicationLog: ReportedMedicationUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.medications_icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = medicationLog.name,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "${medicationLog.form} - ${medicationLog.dosage}",
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = medicationLog.date,
                style = typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview

fun DmtMedicationLogItemPreview() {
    DmtTheme {
         DmtMedicationLogItem(
             medicationLog =
                 ReportedMedicationUiModel(
                     id = "1",
                     name = "CYSTEAMINE DRP 0.55% BOT 5ML",
                     form = "DRP",
                     dosage = "0.0055",
                     date = "2025-10-21 09:44:45"
                 )
         )
     }
}