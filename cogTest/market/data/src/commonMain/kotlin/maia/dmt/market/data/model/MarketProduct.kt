package maia.dmt.market.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MarketResponseContainer(
    @SerialName("measurement_json")
    val data: MarketDataDto
)

@Serializable
data class MarketDataDto(
    val products: List<MarketProductDto>
)


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