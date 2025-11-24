package maia.dmt.market.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import maia.dmt.market.domain.model.MarketCategory
import maia.dmt.market.domain.model.MarketProduct
import maia.dmt.market.domain.repository.MarketRepository

class MarketRepositoryImpl : MarketRepository {

    private val categories = listOf(
        MarketCategory(
            id = "frozen",
            nameResId = "cogTest_market_category_frozen",
            iconResId = "market_frozen_icon"
        ),
        MarketCategory(
            id = "dairy",
            nameResId = "cogTest_market_category_dairy",
            iconResId = "market_dairy_icon"
        ),
        MarketCategory(
            id = "fruits",
            nameResId = "cogTest_market_category_fruits",
            iconResId = "market_fruits_icon"
        ),
        MarketCategory(
            id = "dry_spices",
            nameResId = "cogTest_market_category_dry_spices",
            iconResId = "market_dry_spices_icon"
        ),
        MarketCategory(
            id = "vegetables",
            nameResId = "cogTest_market_category_vegetables",
            iconResId = "market_vegetables_icon"
        ),
        MarketCategory(
            id = "bakery",
            nameResId = "cogTest_market_category_bakery",
            iconResId = "market_bakery_icon"
        ),
        MarketCategory(
            id = "meat",
            nameResId = "cogTest_market_category_meat",
            iconResId = "market_meat_icon"
        ),
        MarketCategory(
            id = "cleaning",
            nameResId = "cogTest_market_category_cleaning_disposable",
            iconResId = "market_cleaning_icon"
        )
    )

    private val allProducts = buildList {
        val dairyCategory = categories.first { it.id == "dairy" }
        addAll(
            listOf(
                MarketProduct("white_cheese_500", dairyCategory, "cogTest_market_item_white_cheese_500", Icons.Default.ShoppingCart),
                MarketProduct("white_cheese_250", dairyCategory, "cogTest_market_item_white_cheese_250", Icons.Default.ShoppingCart),
                MarketProduct("milk_1l_1", dairyCategory, "cogTest_market_item_milk_1_liter_1_percent", Icons.Default.ShoppingCart),
                MarketProduct("milk_1l_3", dairyCategory, "cogTest_market_item_milk_1_liter_3_percent", Icons.Default.ShoppingCart),
                MarketProduct("sweet_cream", dairyCategory, "cogTest_market_item_sweet_cream_38", Icons.Default.ShoppingCart),
                MarketProduct("bulgarian", dairyCategory, "cogTest_market_item_bulgarian_250", Icons.Default.ShoppingCart),
                MarketProduct("yellow_sliced", dairyCategory, "cogTest_market_item_yellow_sliced_250", Icons.Default.ShoppingCart),
                MarketProduct("yellow_grated", dairyCategory, "cogTest_market_item_yellow_grated_500", Icons.Default.ShoppingCart)
            )
        )

        val meatCategory = categories.first { it.id == "meat" }
        addAll(
            listOf(
                MarketProduct("shnitzel", meatCategory, "cogTest_market_item_shnitzel_kg", Icons.Default.ShoppingCart),
                MarketProduct("legs", meatCategory, "cogTest_market_item_legs_4", Icons.Default.ShoppingCart),
                MarketProduct("breast", meatCategory, "cogTest_market_item_breast_kg", Icons.Default.ShoppingCart),
                MarketProduct("pargit", meatCategory, "cogTest_market_item_pargit", Icons.Default.ShoppingCart),
                MarketProduct("beef_ground", meatCategory, "cogTest_market_item_beef_ground_500", Icons.Default.ShoppingCart)
            )
        )

        val vegetablesCategory = categories.first { it.id == "vegetables" }
        addAll(
            listOf(
                MarketProduct("tomato", vegetablesCategory, "cogTest_market_item_tomato_half_kg", Icons.Default.ShoppingCart),
                MarketProduct("cherry_tomato", vegetablesCategory, "cogTest_market_item_cherry_tomato", Icons.Default.ShoppingCart),
                MarketProduct("cucumber", vegetablesCategory, "cogTest_market_item_cucumber_half_kg", Icons.Default.ShoppingCart),
                MarketProduct("broccoli_fresh", vegetablesCategory, "cogTest_market_item_broccoli_fresh_out", Icons.Default.ShoppingCart, isInStock = false),
                MarketProduct("carrot", vegetablesCategory, "cogTest_market_item_carrot_250", Icons.Default.ShoppingCart),
                MarketProduct("onion", vegetablesCategory, "cogTest_market_item_onion", Icons.Default.ShoppingCart),
                MarketProduct("celery", vegetablesCategory, "cogTest_market_item_celery", Icons.Default.ShoppingCart),
                MarketProduct("lemon", vegetablesCategory, "cogTest_market_item_lemon", Icons.Default.ShoppingCart)
            )
        )

        val fruitsCategory = categories.first { it.id == "fruits" }
        addAll(
            listOf(
                MarketProduct("apricot", fruitsCategory, "cogTest_market_item_apricot_pack", Icons.Default.ShoppingCart),
                MarketProduct("banana", fruitsCategory, "cogTest_market_item_banana_100g", Icons.Default.ShoppingCart),
                MarketProduct("nectarine", fruitsCategory, "cogTest_market_item_nectarine", Icons.Default.ShoppingCart),
                MarketProduct("green_apple", fruitsCategory, "cogTest_market_item_green_apple", Icons.Default.ShoppingCart),
                MarketProduct("red_apple", fruitsCategory, "cogTest_market_item_red_apple", Icons.Default.ShoppingCart),
                MarketProduct("melon", fruitsCategory, "cogTest_market_item_melon", Icons.Default.ShoppingCart),
                MarketProduct("grapes", fruitsCategory, "cogTest_market_item_grapes_500", Icons.Default.ShoppingCart)
            )
        )

        val frozenCategory = categories.first { it.id == "frozen" }
        addAll(
            listOf(
                MarketProduct("broccoli_frozen", frozenCategory, "cogTest_market_item_broccoli_frozen", Icons.Default.ShoppingCart),
                MarketProduct("peas_frozen", frozenCategory, "cogTest_market_item_peas_frozen", Icons.Default.ShoppingCart),
                MarketProduct("pea_carrot", frozenCategory, "cogTest_market_item_pea_carrot_mix", Icons.Default.ShoppingCart),
                MarketProduct("cauliflower", frozenCategory, "cogTest_market_item_cauliflower_frozen", Icons.Default.ShoppingCart),
                MarketProduct("green_beans", frozenCategory, "cogTest_market_item_green_beans_frozen", Icons.Default.ShoppingCart),
                MarketProduct("borekas_cheese", frozenCategory, "cogTest_market_item_borekas_cheese", Icons.Default.ShoppingCart),
                MarketProduct("borekas_potato", frozenCategory, "cogTest_market_item_borekas_potato", Icons.Default.ShoppingCart),
                MarketProduct("jahnun", frozenCategory, "cogTest_market_item_jahnun", Icons.Default.ShoppingCart)
            )
        )

        val drySpicesCategory = categories.first { it.id == "dry_spices" }
        addAll(
            listOf(
                MarketProduct("canned_corn", drySpicesCategory, "cogTest_market_item_canned_corn", Icons.Default.ShoppingCart),
                MarketProduct("pickles_vinegar", drySpicesCategory, "cogTest_market_item_pickles_vinegar_250", Icons.Default.ShoppingCart),
                MarketProduct("pickles_salt", drySpicesCategory, "cogTest_market_item_pickles_salt_250", Icons.Default.ShoppingCart),
                MarketProduct("tuna", drySpicesCategory, "cogTest_market_item_tuna_can", Icons.Default.ShoppingCart),
                MarketProduct("baby_corn", drySpicesCategory, "cogTest_market_item_baby_corn", Icons.Default.ShoppingCart),
                MarketProduct("sardines", drySpicesCategory, "cogTest_market_item_sardines", Icons.Default.ShoppingCart),
                MarketProduct("sunflower_oil", drySpicesCategory, "cogTest_market_item_sunflower_oil", Icons.Default.ShoppingCart),
                MarketProduct("soy_oil", drySpicesCategory, "cogTest_market_item_soy_oil", Icons.Default.ShoppingCart),
                MarketProduct("coconut_oil", drySpicesCategory, "cogTest_market_item_coconut_oil", Icons.Default.ShoppingCart),
                MarketProduct("olives", drySpicesCategory, "cogTest_market_item_olives_canned", Icons.Default.ShoppingCart),
                MarketProduct("salt", drySpicesCategory, "cogTest_market_item_salt_basic", Icons.Default.ShoppingCart),
                MarketProduct("sugar", drySpicesCategory, "cogTest_market_item_sugar_basic", Icons.Default.ShoppingCart),
                MarketProduct("black_pepper", drySpicesCategory, "cogTest_market_item_black_pepper_basic", Icons.Default.ShoppingCart),
                MarketProduct("flour", drySpicesCategory, "cogTest_market_item_flour_basic", Icons.Default.ShoppingCart),
                MarketProduct("cacao", drySpicesCategory, "cogTest_market_item_cacao_basic", Icons.Default.ShoppingCart),
                MarketProduct("baking_powder", drySpicesCategory, "cogTest_market_item_baking_powder_basic", Icons.Default.ShoppingCart),
                MarketProduct("vanilla", drySpicesCategory, "cogTest_market_item_vanilla_extract_basic", Icons.Default.ShoppingCart),
                MarketProduct("tomato_paste", drySpicesCategory, "cogTest_market_item_tomato_paste", Icons.Default.ShoppingCart),
                MarketProduct("hummus", drySpicesCategory, "cogTest_market_item_hummus_dry", Icons.Default.ShoppingCart)
            )
        )

        val bakeryCategory = categories.first { it.id == "bakery" }
        addAll(
            listOf(
                MarketProduct("bread_no_sugar", bakeryCategory, "cogTest_market_item_bread_no_sugar", Icons.Default.ShoppingCart),
                MarketProduct("bread_regular", bakeryCategory, "cogTest_market_item_bread_regular", Icons.Default.ShoppingCart),
                MarketProduct("challah", bakeryCategory, "cogTest_market_item_challah", Icons.Default.ShoppingCart),
                MarketProduct("cookies_gluten_free", bakeryCategory, "cogTest_market_item_cookies_gluten_free", Icons.Default.ShoppingCart),
                MarketProduct("yeast_cake", bakeryCategory, "cogTest_market_item_yeast_cake", Icons.Default.ShoppingCart)
            )
        )

        val cleaningCategory = categories.first { it.id == "cleaning" }
        addAll(
            listOf(
                MarketProduct("bleach", cleaningCategory, "cogTest_market_item_bleach", Icons.Default.ShoppingCart),
                MarketProduct("gloves", cleaningCategory, "cogTest_market_item_gloves", Icons.Default.ShoppingCart),
                MarketProduct("dish_soap", cleaningCategory, "cogTest_market_item_dish_soap", Icons.Default.ShoppingCart),
                MarketProduct("window_cleaner", cleaningCategory, "cogTest_market_item_window_cleaner", Icons.Default.ShoppingCart),
                MarketProduct("garbage_bags", cleaningCategory, "cogTest_market_item_garbage_bags", Icons.Default.ShoppingCart),
                MarketProduct("scotch_pack", cleaningCategory, "cogTest_market_item_scotch_pack", Icons.Default.ShoppingCart),
                MarketProduct("disposable_cups", cleaningCategory, "cogTest_market_item_disposable_cups_pack", Icons.Default.ShoppingCart)
            )
        )
    }

    override fun getAllCategories(): List<MarketCategory> = categories

    override fun getProductsByCategory(categoryId: String): List<MarketProduct> {
        return allProducts.filter { it.category.id == categoryId }
    }

    override fun getCategoryById(categoryId: String): MarketCategory? {
        return categories.firstOrNull { it.id == categoryId }
    }
}