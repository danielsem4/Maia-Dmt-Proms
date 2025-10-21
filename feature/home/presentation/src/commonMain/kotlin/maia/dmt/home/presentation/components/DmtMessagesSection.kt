package maia.dmt.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class MessageType {
    INFO, REMINDER, MESSAGE
}

data class Message(
    val text: String,
    val type: MessageType
)


@Composable
fun MessageSlot(message: Message) {
    val extendedColors = MaterialTheme.colorScheme.extended

    val backgroundColor = when (message.type) {
        MessageType.INFO -> extendedColors.cakeBlue
        MessageType.REMINDER -> extendedColors.cakeRed
        MessageType.MESSAGE -> extendedColors.cakeGreen
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}


@Composable
fun DmtMessageSection(
    modifier: Modifier = Modifier,
    title: String = "Title",
    messages: List<Message> = emptyList()
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (messages.isEmpty()) {
                    Text(
                        text = "No messages",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    messages.forEach { message ->
                        MessageSlot(message = message)
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun DmtMessageSectionPreview() {
    DmtTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DmtMessageSection(
                title = "Messages",
                messages = listOf(
                    Message("Your lab results are available.", MessageType.INFO),
                    Message("Reminder: Doctor's appointment tomorrow at 10:00 AM.", MessageType.REMINDER),
                    Message("Take 2 pills with your breakfast.", MessageType.MESSAGE),
                    Message("A new health article is available for you.", MessageType.INFO)
                )
            )
        }
    }
}

@Composable
@Preview
fun DmtMessageSectionEmptyPreview() {
    DmtTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DmtMessageSection(
                title = "Messages",
                messages = emptyList()
            )
        }
    }
}