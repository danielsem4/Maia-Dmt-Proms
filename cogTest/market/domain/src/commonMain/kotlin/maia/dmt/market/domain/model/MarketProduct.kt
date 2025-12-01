package maia.dmt.market.domain.model

data class MarketProduct(
    val id: String,
    val categoryId: String,
    val titleResId: String,
    val iconRes: String,
    val amount: Int = 0,
    val price: Double = 0.0,
    val isInStock: Boolean = true,
    val isDonation: Boolean = false
)