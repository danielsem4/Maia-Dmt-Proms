package maia.dmt.market.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MarketProduct(
    val id: String,
    val category: MarketCategory,
    val titleResId: String,
    val iconRes: ImageVector,
    val amount: Int = 0,
    val isDonation: Boolean = false,
    val isInStock: Boolean = true
)

data class MarketCategory(
    val id: String,
    val nameResId: String,
    val iconResId: String
)

enum class MarketCategoryType {
    DAIRY,
    MEAT,
    VEGETABLES,
    FRUITS,
    FROZEN,
    DRY_SPICES,
    BAKERY,
    CLEANING
}