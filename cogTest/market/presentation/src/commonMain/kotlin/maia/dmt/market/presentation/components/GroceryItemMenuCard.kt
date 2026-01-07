package maia.dmt.market.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_status_out_of_stock
import dmtproms.cogtest.market.presentation.generated.resources.market_minus_icon
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.components.NetworkImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtGroceryItemMenuCard(
    text: String,
    quantity: Int,
    imageUrl: String,
    isOutOfStock: Boolean = false,
    isDonation: Boolean = false,
    price: Double = 0.0,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isOutOfStock -> Color.Gray.copy(alpha = 0.5f)
        isDonation -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when {
        isOutOfStock -> Color.Gray
        isDonation -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable(enabled = !isOutOfStock) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                NetworkImage(
                    imageUrl = imageUrl,
                    contentDescription = text,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            
            Text(
                text = "$text  ₪$price",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            if (!isOutOfStock) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDecrement,
                        enabled = quantity > 0,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.market_minus_icon),
                            contentDescription = "Decrease",
                            tint = if (quantity > 0) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.widthIn(min = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(Res.string.cogTest_market_status_out_of_stock),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun DmtGroceryItemMenuCardPreview() {
    DmtTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DmtGroceryItemMenuCard(
                    text = "עגבניות שרי",
                    quantity = 0,
                    imageUrl = "http://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_tomato.png",
                    onIncrement = {},
                    onDecrement = {},
                    onClick = {}
                )

                DmtGroceryItemMenuCard(
                    text = "קמח לבן",
                    quantity = 3,
                    imageUrl = "http://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_flour.png",
                    onIncrement = {},
                    onDecrement = {},
                    onClick = {}
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DmtGroceryItemMenuCard(
                    text = "לחם אחיד",
                    quantity = 0,
                    imageUrl = "http://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_bread.png",
                    isOutOfStock = true,
                    onIncrement = {},
                    onDecrement = {},
                    onClick = {}
                )

                DmtGroceryItemMenuCard(
                    text = "תרומה לקהילה",
                    quantity = 1,
                    imageUrl = "",
                    isDonation = true,
                    onIncrement = {},
                    onDecrement = {},
                    onClick = {}
                )
            }
        }
    }
}