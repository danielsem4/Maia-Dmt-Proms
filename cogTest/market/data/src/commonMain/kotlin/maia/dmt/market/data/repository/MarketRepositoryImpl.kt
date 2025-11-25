package maia.dmt.market.data.repository

import maia.dmt.market.data.mapper.MarketProductImageResMapper
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
            id = "cleaning_disposable",
            nameResId = "cogTest_market_category_cleaning_disposable",
            iconResId = "market_cleaning_icon"
        )
    )

    private val allProducts = buildList {
        // Dairy Products
        val dairyCategory = categories.first { it.id == "dairy" }
        addAll(
            listOf(
                MarketProduct(
                    id = "white_cheese_500",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_white_cheese_500",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_white_cheese_500"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_white_cheese_500")
                ),
                MarketProduct(
                    id = "white_cheese_250",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_white_cheese_250",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_white_cheese_250"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_white_cheese_250")
                ),
                MarketProduct(
                    id = "milk_1l_1",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_milk_1_liter_1_percent",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_milk_1_liter_1_percent"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_milk_1_liter_1_percent")
                ),
                MarketProduct(
                    id = "milk_1l_3",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_milk_1_liter_3_percent",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_milk_1_liter_3_percent"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_milk_1_liter_3_percent")
                ),
                MarketProduct(
                    id = "sweet_cream",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_sweet_cream_38",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_sweet_cream_38"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_sweet_cream_38")
                ),
                MarketProduct(
                    id = "bulgarian",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_bulgarian_250",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_bulgarian_250"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_bulgarian_250")
                ),
                MarketProduct(
                    id = "yellow_sliced",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_yellow_sliced_250",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_yellow_sliced_250"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_yellow_sliced_250")
                ),
                MarketProduct(
                    id = "yellow_grated",
                    categoryId = dairyCategory.id,
                    titleResId = "cogTest_market_item_yellow_grated_500",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_yellow_grated_500"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_yellow_grated_500")
                )
            )
        )

        // Meat Products
        val meatCategory = categories.first { it.id == "meat" }
        addAll(
            listOf(
                MarketProduct(
                    id = "shnitzel",
                    categoryId = meatCategory.id,
                    titleResId = "cogTest_market_item_shnitzel_kg",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_shnitzel_kg"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_shnitzel_kg")
                ),
                MarketProduct(
                    id = "legs",
                    categoryId = meatCategory.id,
                    titleResId = "cogTest_market_item_legs_4",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_legs_4"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_legs_4")
                ),
                MarketProduct(
                    id = "breast",
                    categoryId = meatCategory.id,
                    titleResId = "cogTest_market_item_breast_kg",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_breast_kg"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_breast_kg")
                ),
                MarketProduct(
                    id = "pargit",
                    categoryId = meatCategory.id,
                    titleResId = "cogTest_market_item_pargit",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_pargit"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_pargit")
                ),
                MarketProduct(
                    id = "beef_ground",
                    categoryId = meatCategory.id,
                    titleResId = "cogTest_market_item_beef_ground_500",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_beef_ground_500"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_beef_ground_500")
                )
            )
        )

        // Vegetables Products
        val vegetablesCategory = categories.first { it.id == "vegetables" }
        addAll(
            listOf(
                MarketProduct(
                    id = "tomato",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_tomato_half_kg",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_tomato_half_kg"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_tomato_half_kg")
                ),
                MarketProduct(
                    id = "cherry_tomato",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_cherry_tomato",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_cherry_tomato"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_cherry_tomato")
                ),
                MarketProduct(
                    id = "cucumber",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_cucumber_half_kg",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_cucumber_half_kg"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_cucumber_half_kg")
                ),
                MarketProduct(
                    id = "broccoli_fresh",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_broccoli_fresh_out",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_broccoli_fresh_out"),
                    isInStock = false,
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_broccoli_fresh_out")
                ),
                MarketProduct(
                    id = "carrot",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_carrot_250",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_carrot_250"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_carrot_250")
                ),
                MarketProduct(
                    id = "onion",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_onion",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_onion"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_onion")
                ),
                MarketProduct(
                    id = "celery",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_celery",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_celery"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_celery")
                ),
                MarketProduct(
                    id = "lemon",
                    categoryId = vegetablesCategory.id,
                    titleResId = "cogTest_market_item_lemon",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_lemon"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_lemon")
                )
            )
        )

        // Fruits Products
        val fruitsCategory = categories.first { it.id == "fruits" }
        addAll(
            listOf(
                MarketProduct(
                    id = "apricot",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_apricot_pack",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_apricot_pack"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_apricot_pack")
                ),
                MarketProduct(
                    id = "banana",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_banana_100g",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_banana_100g"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_banana_100g")
                ),
                MarketProduct(
                    id = "nectarine",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_nectarine",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_nectarine"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_nectarine")
                ),
                MarketProduct(
                    id = "green_apple",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_green_apple",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_green_apple"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_green_apple")
                ),
                MarketProduct(
                    id = "red_apple",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_red_apple",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_red_apple"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_red_apple")
                ),
                MarketProduct(
                    id = "melon",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_melon",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_melon"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_melon")
                ),
                MarketProduct(
                    id = "grapes",
                    categoryId = fruitsCategory.id,
                    titleResId = "cogTest_market_item_grapes_500",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_grapes_500"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_grapes_500")
                )
            )
        )

        // Frozen Products
        val frozenCategory = categories.first { it.id == "frozen" }
        addAll(
            listOf(
                MarketProduct(
                    id = "broccoli_frozen",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_broccoli_frozen",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_broccoli_frozen"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_broccoli_frozen")
                ),
                MarketProduct(
                    id = "peas_frozen",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_peas_frozen",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_peas_frozen"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_peas_frozen")
                ),
                MarketProduct(
                    id = "pea_carrot",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_pea_carrot_mix",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_pea_carrot_mix"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_pea_carrot_mix")
                ),
                MarketProduct(
                    id = "cauliflower",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_cauliflower_frozen",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_cauliflower_frozen"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_cauliflower_frozen")
                ),
                MarketProduct(
                    id = "green_beans",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_green_beans_frozen",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_green_beans_frozen"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_green_beans_frozen")
                ),
                MarketProduct(
                    id = "borekas_cheese",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_borekas_cheese",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_borekas_cheese"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_borekas_cheese")
                ),
                MarketProduct(
                    id = "borekas_potato",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_borekas_potato",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_borekas_potato"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_borekas_potato")
                ),
                MarketProduct(
                    id = "jahnun",
                    categoryId = frozenCategory.id,
                    titleResId = "cogTest_market_item_jahnun",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_jahnun"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_jahnun")
                )
            )
        )

        // Dry & Spices Products
        val drySpicesCategory = categories.first { it.id == "dry_spices" }
        addAll(
            listOf(
                MarketProduct(
                    id = "canned_corn",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_canned_corn",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_canned_corn"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_canned_corn")
                ),
                MarketProduct(
                    id = "pickles_vinegar",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_pickles_vinegar_250",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_pickles_vinegar_250"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_pickles_vinegar_250")
                ),
                MarketProduct(
                    id = "pickles_salt",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_pickles_salt_250",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_pickles_salt_250"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_pickles_salt_250")
                ),
                MarketProduct(
                    id = "tuna",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_tuna_can",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_tuna_can"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_tuna_can")
                ),
                MarketProduct(
                    id = "baby_corn",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_baby_corn",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_baby_corn"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_baby_corn")
                ),
                MarketProduct(
                    id = "sardines",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_sardines",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_sardines"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_sardines")
                ),
                MarketProduct(
                    id = "sunflower_oil",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_sunflower_oil",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_sunflower_oil"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_sunflower_oil")
                ),
                MarketProduct(
                    id = "soy_oil",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_soy_oil",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_soy_oil"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_soy_oil")
                ),
                MarketProduct(
                    id = "coconut_oil",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_coconut_oil",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_coconut_oil"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_coconut_oil")
                ),
                MarketProduct(
                    id = "olives",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_olives_canned",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_olives_canned"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_olives_canned")
                ),
                MarketProduct(
                    id = "salt",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_salt_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_salt_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_salt_basic")
                ),
                MarketProduct(
                    id = "sugar",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_sugar_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_sugar_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_sugar_basic")
                ),
                MarketProduct(
                    id = "black_pepper",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_black_pepper_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_black_pepper_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_black_pepper_basic")
                ),
                MarketProduct(
                    id = "flour",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_flour_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_flour_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_flour_basic")
                ),
                MarketProduct(
                    id = "cacao",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_cacao_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_cacao_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_cacao_basic")
                ),
                MarketProduct(
                    id = "baking_powder",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_baking_powder_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_baking_powder_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_baking_powder_basic")
                ),
                MarketProduct(
                    id = "vanilla",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_vanilla_extract_basic",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_vanilla_extract_basic"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_vanilla_extract_basic")
                ),
                MarketProduct(
                    id = "tomato_paste",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_tomato_paste",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_tomato_paste"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_tomato_paste")
                ),
                MarketProduct(
                    id = "hummus",
                    categoryId = drySpicesCategory.id,
                    titleResId = "cogTest_market_item_hummus_dry",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_hummus_dry"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_hummus_dry")
                )
            )
        )

        // Bakery Products
        val bakeryCategory = categories.first { it.id == "bakery" }
        addAll(
            listOf(
                MarketProduct(
                    id = "bread_no_sugar",
                    categoryId = bakeryCategory.id,
                    titleResId = "cogTest_market_item_bread_no_sugar",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_bread_no_sugar"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_bread_no_sugar")
                ),
                MarketProduct(
                    id = "bread_regular",
                    categoryId = bakeryCategory.id,
                    titleResId = "cogTest_market_item_bread_regular",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_bread_regular"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_bread_regular")
                ),
                MarketProduct(
                    id = "challah",
                    categoryId = bakeryCategory.id,
                    titleResId = "cogTest_market_item_challah",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_challah"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_challah")
                ),
                MarketProduct(
                    id = "cookies_gluten_free",
                    categoryId = bakeryCategory.id,
                    titleResId = "cogTest_market_item_cookies_gluten_free",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_cookies_gluten_free"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_cookies_gluten_free")
                ),
                MarketProduct(
                    id = "yeast_cake",
                    categoryId = bakeryCategory.id,
                    titleResId = "cogTest_market_item_yeast_cake",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_yeast_cake"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_yeast_cake")
                )
            )
        )

        // Cleaning Products
        val cleaningCategory = categories.first { it.id == "cleaning_disposable" }
        addAll(
            listOf(
                MarketProduct(
                    id = "bleach",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_bleach",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_bleach"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_bleach")
                ),
                MarketProduct(
                    id = "gloves",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_gloves",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_gloves"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_gloves")
                ),
                MarketProduct(
                    id = "dish_soap",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_dish_soap",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_dish_soap"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_dish_soap")
                ),
                MarketProduct(
                    id = "window_cleaner",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_window_cleaner",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_window_cleaner"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_window_cleaner")
                ),
                MarketProduct(
                    id = "garbage_bags",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_garbage_bags",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_garbage_bags"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_garbage_bags")
                ),
                MarketProduct(
                    id = "scotch_pack",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_scotch_pack",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_scotch_pack"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_scotch_pack")
                ),
                MarketProduct(
                    id = "disposable_cups",
                    categoryId = cleaningCategory.id,
                    titleResId = "cogTest_market_item_disposable_cups_pack",
                    iconRes = MarketProductImageResMapper.getProductImageResId("cogTest_market_item_disposable_cups_pack"),
                    isDonation = MarketProductImageResMapper.isDonationProduct("cogTest_market_item_disposable_cups_pack")
                )
            )
        )
    }

    override fun getAllCategories(): List<MarketCategory> = categories

    override fun getProductsByCategory(categoryId: String): List<MarketProduct> {
        return allProducts.filter { it.categoryId == categoryId }
    }

    override fun getCategoryById(categoryId: String): MarketCategory? {
        return categories.firstOrNull { it.id == categoryId }
    }
}