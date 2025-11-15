package maia.dmt.market.data.mapper

import maia.dmt.market.data.model.GroceryEntity
import maia.dmt.market.data.model.RecipeEntity
import maia.dmt.market.domain.model.Grocery
import maia.dmt.market.domain.model.RecipeData

class RecipeDataMapper {

    fun toDomain(entity: RecipeEntity): RecipeData {
        return RecipeData(
            id = entity.id,
            groceryIds = entity.groceryIds
        )
    }

    fun toEntity(domain: RecipeData): RecipeEntity {
        return RecipeEntity(
            id = domain.id,
            groceryIds = domain.groceryIds
        )
    }

    fun groceryToDomain(entity: GroceryEntity): Grocery {
        return Grocery(id = entity.id)
    }

    fun groceryToEntity(domain: Grocery): GroceryEntity {
        return GroceryEntity(id = domain.id)
    }
}