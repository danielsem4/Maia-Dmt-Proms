package maia.dmt.market.data.mapper

import maia.dmt.market.data.model.MarketProductDto
import maia.dmt.market.domain.model.MarketProduct

fun MarketProductDto.toDomain(): MarketProduct {
    return MarketProduct(
        id = id.toString(),
        titleResId = name,
        isDonation = isDonation,
        price = price,
        isInStock = inStock,
        categoryId = category,
        amount = amount,
        iconRes = imageUrl
    )
}

fun List<MarketProductDto>.toDomain(): List<MarketProduct> {
    return map { it.toDomain() }
}