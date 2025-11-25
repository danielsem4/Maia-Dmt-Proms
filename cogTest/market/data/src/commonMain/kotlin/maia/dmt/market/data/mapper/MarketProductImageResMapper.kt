package maia.dmt.market.data.mapper

object MarketProductImageResMapper {

    fun getProductImageResId(titleResId: String): String {
        return when (titleResId) {
            // Dairy
            "cogTest_market_item_white_cheese_500" -> "market_white_cheese"
            "cogTest_market_item_white_cheese_250" -> "market_white_cheese"
            "cogTest_market_item_milk_1_liter_1_percent" -> "market_milk1precent"
            "cogTest_market_item_milk_1_liter_3_percent" -> "milk3precent"
            "cogTest_market_item_sweet_cream_38" -> "market_milk_sweet_shamenet"
            "cogTest_market_item_bulgarian_250" -> "market_bulgarian_cheese"
            "cogTest_market_item_yellow_sliced_250" -> "market_yellow_cheese"
            "cogTest_market_item_yellow_grated_500" -> "market_yellowcheese_megurad"

            // Meat
            "cogTest_market_item_shnitzel_kg" -> "market_meat_shnitzel"
            "cogTest_market_item_legs_4" -> "market_meat_chickenshok"
            "cogTest_market_item_breast_kg" -> "market_meat_chickenbreast"
            "cogTest_market_item_pargit" -> "market_meat_pargit"
            "cogTest_market_item_beef_ground_500" -> "market_meat_tahun"

            // Vegetables
            "cogTest_market_item_tomato_half_kg" -> "market_tomato"
            "cogTest_market_item_cherry_tomato" -> "market_vegetables_cherrytomato"
            "cogTest_market_item_cucumber_half_kg" -> "market_cucumber"
            "cogTest_market_item_broccoli_fresh_out" -> "market_broccoli"
            "cogTest_market_item_carrot_250" -> "market_vegetables_carrot"
            "cogTest_market_item_onion" -> "market_vegetables_onion"
            "cogTest_market_item_celery" -> "market_vegetables_cellery"
            "cogTest_market_item_lemon" -> "market_lemon"

            // Fruits
            "cogTest_market_item_apricot_pack" -> "market_fruits_mishmish"
            "cogTest_market_item_banana_100g" -> "market_fruits_banana"
            "cogTest_market_item_nectarine" -> "market_fruits_peach"
            "cogTest_market_item_green_apple" -> "market_fruits_greenapple"
            "cogTest_market_item_red_apple" -> "market_fruits_redapple"
            "cogTest_market_item_melon" -> "market_fruits_melon"
            "cogTest_market_item_grapes_500" -> "market_fruits_grapes"

            // Frozen
            "cogTest_market_item_broccoli_frozen" -> "market_frozen_brocoli"
            "cogTest_market_item_peas_frozen" -> "market_frozen_afuna"
            "cogTest_market_item_pea_carrot_mix" -> "market_frozen_carrotafuna"
            "cogTest_market_item_cauliflower_frozen" -> "market_frozen_carrotafuna"
            "cogTest_market_item_green_beans_frozen" -> "market_frozen_beans"
            "cogTest_market_item_borekas_cheese" -> "market_frozen_borekascheese"
            "cogTest_market_item_borekas_potato" -> "market_frozen_borekastapha"
            "cogTest_market_item_jahnun" -> "market_frozen_jahnun"

            // Dry & Spices
            "cogTest_market_item_canned_corn" -> "market_dry_cannedcorn"
            "cogTest_market_item_pickles_vinegar_250" -> "market_dry_pickle"
            "cogTest_market_item_pickles_salt_250" -> "market_dry_pickle"
            "cogTest_market_item_tuna_can" -> "market_dry_tuna"
            "cogTest_market_item_baby_corn" -> "market_dry_littlecorn"
            "cogTest_market_item_sardines" -> "market_dry_sardine"
            "cogTest_market_item_sunflower_oil" -> "market_kanola_oil"
            "cogTest_market_item_soy_oil" -> "market_dry_oilsoya"
            "cogTest_market_item_coconut_oil" -> "market_olive_oil"
            "cogTest_market_item_olives_canned" -> "market_olives"
            "cogTest_market_item_salt_basic" -> "market_salt"
            "cogTest_market_item_sugar_basic" -> "market_suger"
            "cogTest_market_item_black_pepper_basic" -> "market_black_pepper"
            "cogTest_market_item_flour_basic" -> "market_flour"
            "cogTest_market_item_cacao_basic" -> "market_cacao"
            "cogTest_market_item_baking_powder_basic" -> "market_baking_powder"
            "cogTest_market_item_vanilla_extract_basic" -> "market_vanil"
            "cogTest_market_item_tomato_paste" -> "market_dry_tomatopaste"
            "cogTest_market_item_hummus_dry" -> "market_dry_hummus"

            // Bakery
            "cogTest_market_item_bread_no_sugar" -> "market_bakery_nosuger_bread"
            "cogTest_market_item_bread_regular" -> "market_bakery_bread"
            "cogTest_market_item_challah" -> "market_bakery_halla"
            "cogTest_market_item_cookies_gluten_free" -> "market_bakery_cookies"
            "cogTest_market_item_yeast_cake" -> "market_bakery_cake"

            // Cleaning
            "cogTest_market_item_bleach" -> "market_clean_economica"
            "cogTest_market_item_gloves" -> "market_clean_gloves"
            "cogTest_market_item_dish_soap" -> "market_clean_dishsabon"
            "cogTest_market_item_window_cleaner" -> "market_clean_forwindows"
            "cogTest_market_item_garbage_bags" -> "market_clean_garbagebag"
            "cogTest_market_item_scotch_pack" -> "market_clean_scotch"
            "cogTest_market_item_disposable_cups_pack" -> "market_clean_cups"

            else -> "market_white_cheese"
        }
    }

    fun isDonationProduct(titleResId: String): Boolean {
        return when (titleResId) {
            "cogTest_market_item_challah",
            "cogTest_market_item_tomato_paste",
            "cogTest_market_item_coconut_oil",
            "cogTest_market_item_milk_1_liter_3_percent",
            "cogTest_market_item_olives_canned",
            "cogTest_market_item_hummus_dry",
            "cogTest_market_item_disposable_cups_pack",
            "cogTest_market_item_dish_soap" -> true
            else -> false
        }
    }
}