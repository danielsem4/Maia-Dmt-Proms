package maia.dmt.market.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtMessageCard(
    modifier: Modifier = Modifier,
    messageText: String = "Hello, World!",
    timestamp: String = "9:27",
    isOwnMessage: Boolean = true,
    author: String = "Author",
) {
    val containerColor = if (isOwnMessage) Color(0xFFE7FFDB) else Color.White
    val contentColor = Color.Black
    val metaColor = Color.Gray

    val shape = if (isOwnMessage) SentMessageShape else ReceivedMessageShape

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = shape,
            color = containerColor,
            shadowElevation = 1.dp,
            modifier = Modifier
                .widthIn(max = 320.dp) // Prevent full width
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 6.dp)
            ) {
                if (!isOwnMessage && author.isNotEmpty()) {
                    Text(
                        text = author,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFD66616),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Text(
                    text = messageText,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = metaColor
                    )

                    if (isOwnMessage) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Read",
                            tint = Color(0xFF53BDEB),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}


private val SentMessageShape: Shape = GenericShape { size, _ ->
    val cornerRadius = 20f
    val tailWidth = 15f
    val tailHeight = 25f


    moveTo(cornerRadius, 0f)
    lineTo(size.width - cornerRadius, 0f)
    arcTo(
        rect = Rect(
            left = size.width - 2 * cornerRadius,
            top = 0f,
            right = size.width,
            bottom = 2 * cornerRadius
        ),
        startAngleDegrees = 270f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )
    lineTo(size.width, size.height - cornerRadius)

    cubicTo(
        x1 = size.width, y1 = size.height + 5f,
        x2 = size.width + tailWidth, y2 = size.height,
        x3 = size.width - cornerRadius, y3 = size.height 
    )


    lineTo(cornerRadius, size.height)
    arcTo(
        rect = Rect(0f, size.height - 2 * cornerRadius, 2 * cornerRadius, size.height),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )
    lineTo(0f, cornerRadius)
    arcTo(
        rect = Rect(0f, 0f, 2 * cornerRadius, 2 * cornerRadius),
        startAngleDegrees = 180f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )
    close()
}

private val ReceivedMessageShape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 12.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

@Composable
@Preview
fun DmtMessageCardPreview() {
    DmtTheme {
        Column(modifier = Modifier.padding(16.dp).background(Color(0xFFEFEFEF))) {
            DmtMessageCard(
                messageText = "תשלחי לי את התמונות של הדירה",
                timestamp = "9:27",
                isOwnMessage = true
            )

            Spacer(modifier = Modifier.height(8.dp))
            
            DmtMessageCard(
                messageText = "No problem, sending them now!",
                timestamp = "9:28",
                isOwnMessage = false,
                author = "Broker"
            )
        }
    }
}