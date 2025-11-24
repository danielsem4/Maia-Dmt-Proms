package maia.dmt.market.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.market_minus_icon
import maia.dmt.core.designsystem.theme.DmtTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DmtGroceryItemMenuCard(
    modifier: Modifier = Modifier,
    text: String,
    quantity: Int = 0,
    imageVector: ImageVector = Icons.Default.ShoppingCart,
    isOutOfStock: Boolean = false,
    isDonation: Boolean = false,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    // Define colors based on state
    val backgroundColor = if (isDonation) Color(0xFFE3F2FD) else MaterialTheme.colorScheme.surface
    val contentAlpha = if (isOutOfStock) 0.4f else 1f

    Surface(
        modifier = modifier
            .width(200.dp)
            .clip(shape)
            .clickable(enabled = !isOutOfStock, onClick = onClick),
        shape = shape,
        color = backgroundColor,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Out of stock Text Overlay (Always visible if true, pushed to top)
            if (isOutOfStock) {
                Text(
                    text = "אזל מהמלאי",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFD32F2F), // Red color
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Wrapper for main content to apply alpha when disabled
            Column(
                modifier = Modifier.alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onDecrement,
                        enabled = quantity > 0 && !isOutOfStock,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.size(40.dp).clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.market_minus_icon),
                            contentDescription = "Decrease quantity",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(
                        onClick = onIncrement,
                        enabled = !isOutOfStock,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.size(40.dp).clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DmtGroceryItemMenuCardPreview() {
    DmtTheme {
            DmtGroceryItemMenuCard(
                text = "Standard Item",
                quantity = 1,
                onClick = {}
            )
    }
}

@Composable
@Preview
fun DmtGroceryItemMenuCardOutOfPreview() {
    DmtTheme {
        DmtGroceryItemMenuCard(
            text = "גבינה צפתית מעודנת 5% - 250 גרם",
            quantity = 0,
            isOutOfStock = true,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun DmtGroceryItemMenuCardDonationPreview() {
    DmtTheme {
        DmtGroceryItemMenuCard(
            text = "Donation Item",
            quantity = 0,
            isDonation = true,
            onClick = {}
        )
    }
}