package maia.dmt.pass.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.pass.presentation.generated.resources.Res
import dmtproms.cogtest.pass.presentation.generated.resources.pass_message
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ContactActionItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
fun ContactActionItemPreview() {
    DmtTheme {
        ContactActionItem(
            icon = painterResource(Res.drawable.pass_message),
            text = "הודעות",
            onClick = {}
        )
    }
}