package maia.dmt.market.data.mapper

import maia.dmt.market.data.model.MarketProductDto
import maia.dmt.market.domain.model.MarketProduct

object ProductMapper {
    fun toDomain(apiProduct: MarketProductDto): MarketProduct {
        return MarketProduct(
            id = apiProduct.id.toString(),
            categoryId = apiProduct.category,
            titleResId = apiProduct.name,
            iconRes = apiProduct.imageUrl,
            amount = apiProduct.amount,
            isInStock = apiProduct.inStock,
            isDonation = apiProduct.isDonation
        )
    }

    fun toDomainList(apiProducts: List<MarketProductDto>): List<MarketProduct> {
        return apiProducts.map { toDomain(it) }
    }
}
