package maia.dmt.market.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.presentation.components.NetworkImage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtCartItemCard(
    itemName: String,
    itemImageUrl: String,
    quantity: Int,
    itemPrice: String = "10",
    isDonation: Boolean = false,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = if (!isDonation) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        } else {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "הסרה",
                        tint = Color.Red
                    )
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onQuantityIncrease,
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFE8E4F3))
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "הוסף", tint = Color(0xFF6750A4), modifier = Modifier.size(18.dp))
                }

                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = onQuantityDecrease,
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFE8E8E8)),
                    enabled = quantity > 1
                ) {
                    Text(
                        text = "−",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (quantity > 1) Color.Black else Color.Gray
                    )
                }
            }
            Text(
                text = itemPrice,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1.5f)
            ) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                    maxLines = 2
                )

                NetworkImage(
                    imageUrl = itemImageUrl,
                    contentDescription = itemName,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
@Composable
@Preview
fun DmtCartItemCardPreview() {
    DmtTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DmtCartItemCard(
                itemName = "עגבניות שרי",
                itemImageUrl = "http://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_tomato.png",
                quantity = 1,
                onQuantityIncrease = {},
                onQuantityDecrease = {},
                onRemove = {}
            )

            DmtCartItemCard(
                itemName = "ברוקולי קפוא",
                itemImageUrl = "http://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_brocoli.png",
                quantity = 8,
                onQuantityIncrease = {},
                onQuantityDecrease = {},
                onRemove = {}
            )

            DmtCartItemCard(
                itemName = "ברוקולי קפוא",
                itemImageUrl = "http://generic2dev.hitheal.org.il/static/measurements/market%20test/images/market_frozen_brocoli.png",
                quantity = 8,
                isDonation = true,
                onQuantityIncrease = {},
                onQuantityDecrease = {},
                onRemove = {}
            )
        }
    }
}