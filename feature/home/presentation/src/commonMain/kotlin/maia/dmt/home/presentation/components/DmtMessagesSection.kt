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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.no_messages
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class MessageType {
    INFO, REMINDER, MESSAGE
}

data class Message(
    val text: String,
    val type: MessageType
)

data class MessageSizing(
    val cornerRadius: Dp,
    val slotHorizontalPadding: Dp,
    val slotVerticalPadding: Dp,
    val sectionHorizontalPadding: Dp,
    val sectionVerticalPadding: Dp,
    val titlePadding: Dp,
    val spacing: Dp,
    val titleStyle: TextStyle,
    val messageStyle: TextStyle
)

@Composable
fun MessageSlot(message: Message, sizing: MessageSizing) {
    val extendedColors = MaterialTheme.colorScheme.extended

    val backgroundColor = when (message.type) {
        MessageType.INFO -> extendedColors.cakeBlue
        MessageType.REMINDER -> extendedColors.cakeRed
        MessageType.MESSAGE -> extendedColors.cakeGreen
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(sizing.cornerRadius))
            .background(backgroundColor)
            .padding(
                horizontal = sizing.slotHorizontalPadding,
                vertical = sizing.slotVerticalPadding
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message.text,
            style = sizing.messageStyle,
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
    val deviceConfig = currentDeviceConfiguration()

    val sizing = when (deviceConfig) {
        DeviceConfiguration.MOBILE_PORTRAIT -> MessageSizing(
            cornerRadius = 16.dp,
            slotHorizontalPadding = 12.dp,
            slotVerticalPadding = 10.dp,
            sectionHorizontalPadding = 16.dp,
            sectionVerticalPadding = 12.dp,
            titlePadding = 10.dp,
            spacing = 8.dp,
            titleStyle = MaterialTheme.typography.titleMedium,
            messageStyle = MaterialTheme.typography.bodyMedium
        )
        DeviceConfiguration.MOBILE_LANDSCAPE -> MessageSizing(
            cornerRadius = 16.dp,
            slotHorizontalPadding = 16.dp,
            slotVerticalPadding = 12.dp,
            sectionHorizontalPadding = 20.dp,
            sectionVerticalPadding = 12.dp,
            titlePadding = 10.dp,
            spacing = 10.dp,
            titleStyle = MaterialTheme.typography.titleLarge,
            messageStyle = MaterialTheme.typography.bodyLarge
        )
        DeviceConfiguration.TABLET_PORTRAIT -> MessageSizing(
            cornerRadius = 24.dp,
            slotHorizontalPadding = 20.dp,
            slotVerticalPadding = 14.dp,
            sectionHorizontalPadding = 24.dp,
            sectionVerticalPadding = 16.dp,
            titlePadding = 14.dp,
            spacing = 12.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            messageStyle = MaterialTheme.typography.titleMedium
        )
        DeviceConfiguration.TABLET_LANDSCAPE -> MessageSizing(
            cornerRadius = 24.dp,
            slotHorizontalPadding = 24.dp,
            slotVerticalPadding = 16.dp,
            sectionHorizontalPadding = 32.dp,
            sectionVerticalPadding = 20.dp,
            titlePadding = 16.dp,
            spacing = 14.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            messageStyle = MaterialTheme.typography.titleLarge
        )
        DeviceConfiguration.DESKTOP -> MessageSizing(
            cornerRadius = 28.dp,
            slotHorizontalPadding = 24.dp,
            slotVerticalPadding = 18.dp,
            sectionHorizontalPadding = 32.dp,
            sectionVerticalPadding = 24.dp,
            titlePadding = 18.dp,
            spacing = 16.dp,
            titleStyle = MaterialTheme.typography.headlineLarge,
            messageStyle = MaterialTheme.typography.titleLarge
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), // You can optionally scale this outer padding too
        shape = RoundedCornerShape(sizing.cornerRadius),
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
                    .clip(RoundedCornerShape(topStart = sizing.cornerRadius, topEnd = sizing.cornerRadius))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = sizing.titlePadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = sizing.titleStyle,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = sizing.sectionHorizontalPadding,
                        vertical = sizing.sectionVerticalPadding
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(sizing.spacing)
            ) {
                if (messages.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.no_messages),
                        style = sizing.messageStyle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    messages.forEach { message ->
                        MessageSlot(message = message, sizing = sizing)
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