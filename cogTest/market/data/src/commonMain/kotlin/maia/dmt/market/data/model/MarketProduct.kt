package maia.dmt.market.data.model

import kotlinx.serialization.Serializable


@Serializable
data class MarketProductDto(
    val id: Int,
    val name: String,
    val isDonation: Boolean,
    val price: Double,
    val inStock: Boolean,
    val category: String,
    val amount: Int,
    val imageUrl: String
)

@Serializable
data class ProductsResponse(
    val products: List<MarketProductDto>
)
