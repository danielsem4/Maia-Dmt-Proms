package maia.dmt.market.presentation.util

import androidx.compose.runtime.Composable
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_bakery
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_cleaning_disposable
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_dairy
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_dry_spices
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_frozen
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_fruits
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_meat
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_category_vegetables
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_apricot_pack
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_baby_corn
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_baking_powder_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_banana_100g
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_beef_ground_500
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_black_pepper_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_bleach
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_borekas_cheese
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_borekas_potato
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_bread_no_sugar
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_bread_regular
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_breast_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_broccoli_fresh_out
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_broccoli_frozen
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_bulgarian_250
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cacao_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_canned_corn
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_carrot_250
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cauliflower_frozen
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_celery
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_challah
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cherry_tomato
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_coconut_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cookies_gluten_free
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cucumber_half_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_dish_soap
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_disposable_cups_pack
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_flour_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_garbage_bags
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_gloves
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_grapes_500
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_green_apple
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_green_beans_frozen
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_hummus_dry
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_jahnun
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_legs_4
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_lemon
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_melon
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_milk_1_liter_1_percent
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_milk_1_liter_3_percent
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_nectarine
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_olives_canned
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_onion
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_pargit
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_pea_carrot_mix
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_peas_frozen
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_pickles_salt_250
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_pickles_vinegar_250
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_red_apple
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_salt_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_sardines
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_scotch_pack
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_shnitzel_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_soy_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_sugar_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_sunflower_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_sweet_cream_38
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tomato_half_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tomato_paste
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tuna_can
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_vanilla_extract_basic
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_white_cheese_250
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_white_cheese_500
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_window_cleaner
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_yeast_cake
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_yellow_grated_500
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_yellow_sliced_250
import org.jetbrains.compose.resources.stringResource

object MarketStringResourceMapper {
    @Composable
    fun getProductName(titleResId: String): String {
        return when (titleResId) {
            // Dairy
            "cogTest_market_item_white_cheese_500" -> stringResource(Res.string.cogTest_market_item_white_cheese_500)
            "cogTest_market_item_white_cheese_250" -> stringResource(Res.string.cogTest_market_item_white_cheese_250)
            "cogTest_market_item_milk_1_liter_1_percent" -> stringResource(Res.string.cogTest_market_item_milk_1_liter_1_percent)
            "cogTest_market_item_milk_1_liter_3_percent" -> stringResource(Res.string.cogTest_market_item_milk_1_liter_3_percent)
            "cogTest_market_item_sweet_cream_38" -> stringResource(Res.string.cogTest_market_item_sweet_cream_38)
            "cogTest_market_item_bulgarian_250" -> stringResource(Res.string.cogTest_market_item_bulgarian_250)
            "cogTest_market_item_yellow_sliced_250" -> stringResource(Res.string.cogTest_market_item_yellow_sliced_250)
            "cogTest_market_item_yellow_grated_500" -> stringResource(Res.string.cogTest_market_item_yellow_grated_500)

            // Meat
            "cogTest_market_item_shnitzel_kg" -> stringResource(Res.string.cogTest_market_item_shnitzel_kg)
            "cogTest_market_item_legs_4" -> stringResource(Res.string.cogTest_market_item_legs_4)
            "cogTest_market_item_breast_kg" -> stringResource(Res.string.cogTest_market_item_breast_kg)
            "cogTest_market_item_pargit" -> stringResource(Res.string.cogTest_market_item_pargit)
            "cogTest_market_item_beef_ground_500" -> stringResource(Res.string.cogTest_market_item_beef_ground_500)

            // Vegetables
            "cogTest_market_item_tomato_half_kg" -> stringResource(Res.string.cogTest_market_item_tomato_half_kg)
            "cogTest_market_item_cherry_tomato" -> stringResource(Res.string.cogTest_market_item_cherry_tomato)
            "cogTest_market_item_cucumber_half_kg" -> stringResource(Res.string.cogTest_market_item_cucumber_half_kg)
            "cogTest_market_item_broccoli_fresh_out" -> stringResource(Res.string.cogTest_market_item_broccoli_fresh_out)
            "cogTest_market_item_carrot_250" -> stringResource(Res.string.cogTest_market_item_carrot_250)
            "cogTest_market_item_onion" -> stringResource(Res.string.cogTest_market_item_onion)
            "cogTest_market_item_celery" -> stringResource(Res.string.cogTest_market_item_celery)
            "cogTest_market_item_lemon" -> stringResource(Res.string.cogTest_market_item_lemon)

            // Fruits
            "cogTest_market_item_apricot_pack" -> stringResource(Res.string.cogTest_market_item_apricot_pack)
            "cogTest_market_item_banana_100g" -> stringResource(Res.string.cogTest_market_item_banana_100g)
            "cogTest_market_item_nectarine" -> stringResource(Res.string.cogTest_market_item_nectarine)
            "cogTest_market_item_green_apple" -> stringResource(Res.string.cogTest_market_item_green_apple)
            "cogTest_market_item_red_apple" -> stringResource(Res.string.cogTest_market_item_red_apple)
            "cogTest_market_item_melon" -> stringResource(Res.string.cogTest_market_item_melon)
            "cogTest_market_item_grapes_500" -> stringResource(Res.string.cogTest_market_item_grapes_500)

            // Frozen
            "cogTest_market_item_broccoli_frozen" -> stringResource(Res.string.cogTest_market_item_broccoli_frozen)
            "cogTest_market_item_peas_frozen" -> stringResource(Res.string.cogTest_market_item_peas_frozen)
            "cogTest_market_item_pea_carrot_mix" -> stringResource(Res.string.cogTest_market_item_pea_carrot_mix)
            "cogTest_market_item_cauliflower_frozen" -> stringResource(Res.string.cogTest_market_item_cauliflower_frozen)
            "cogTest_market_item_green_beans_frozen" -> stringResource(Res.string.cogTest_market_item_green_beans_frozen)
            "cogTest_market_item_borekas_cheese" -> stringResource(Res.string.cogTest_market_item_borekas_cheese)
            "cogTest_market_item_borekas_potato" -> stringResource(Res.string.cogTest_market_item_borekas_potato)
            "cogTest_market_item_jahnun" -> stringResource(Res.string.cogTest_market_item_jahnun)

            // Dry & Spices
            "cogTest_market_item_canned_corn" -> stringResource(Res.string.cogTest_market_item_canned_corn)
            "cogTest_market_item_pickles_vinegar_250" -> stringResource(Res.string.cogTest_market_item_pickles_vinegar_250)
            "cogTest_market_item_pickles_salt_250" -> stringResource(Res.string.cogTest_market_item_pickles_salt_250)
            "cogTest_market_item_tuna_can" -> stringResource(Res.string.cogTest_market_item_tuna_can)
            "cogTest_market_item_baby_corn" -> stringResource(Res.string.cogTest_market_item_baby_corn)
            "cogTest_market_item_sardines" -> stringResource(Res.string.cogTest_market_item_sardines)
            "cogTest_market_item_sunflower_oil" -> stringResource(Res.string.cogTest_market_item_sunflower_oil)
            "cogTest_market_item_soy_oil" -> stringResource(Res.string.cogTest_market_item_soy_oil)
            "cogTest_market_item_coconut_oil" -> stringResource(Res.string.cogTest_market_item_coconut_oil)
            "cogTest_market_item_olives_canned" -> stringResource(Res.string.cogTest_market_item_olives_canned)
            "cogTest_market_item_salt_basic" -> stringResource(Res.string.cogTest_market_item_salt_basic)
            "cogTest_market_item_sugar_basic" -> stringResource(Res.string.cogTest_market_item_sugar_basic)
            "cogTest_market_item_black_pepper_basic" -> stringResource(Res.string.cogTest_market_item_black_pepper_basic)
            "cogTest_market_item_flour_basic" -> stringResource(Res.string.cogTest_market_item_flour_basic)
            "cogTest_market_item_cacao_basic" -> stringResource(Res.string.cogTest_market_item_cacao_basic)
            "cogTest_market_item_baking_powder_basic" -> stringResource(Res.string.cogTest_market_item_baking_powder_basic)
            "cogTest_market_item_vanilla_extract_basic" -> stringResource(Res.string.cogTest_market_item_vanilla_extract_basic)
            "cogTest_market_item_tomato_paste" -> stringResource(Res.string.cogTest_market_item_tomato_paste)
            "cogTest_market_item_hummus_dry" -> stringResource(Res.string.cogTest_market_item_hummus_dry)

            // Bakery
            "cogTest_market_item_bread_no_sugar" -> stringResource(Res.string.cogTest_market_item_bread_no_sugar)
            "cogTest_market_item_bread_regular" -> stringResource(Res.string.cogTest_market_item_bread_regular)
            "cogTest_market_item_challah" -> stringResource(Res.string.cogTest_market_item_challah)
            "cogTest_market_item_cookies_gluten_free" -> stringResource(Res.string.cogTest_market_item_cookies_gluten_free)
            "cogTest_market_item_yeast_cake" -> stringResource(Res.string.cogTest_market_item_yeast_cake)

            // Cleaning
            "cogTest_market_item_bleach" -> stringResource(Res.string.cogTest_market_item_bleach)
            "cogTest_market_item_gloves" -> stringResource(Res.string.cogTest_market_item_gloves)
            "cogTest_market_item_dish_soap" -> stringResource(Res.string.cogTest_market_item_dish_soap)
            "cogTest_market_item_window_cleaner" -> stringResource(Res.string.cogTest_market_item_window_cleaner)
            "cogTest_market_item_garbage_bags" -> stringResource(Res.string.cogTest_market_item_garbage_bags)
            "cogTest_market_item_scotch_pack" -> stringResource(Res.string.cogTest_market_item_scotch_pack)
            "cogTest_market_item_disposable_cups_pack" -> stringResource(Res.string.cogTest_market_item_disposable_cups_pack)

            else -> titleResId
        }
    }

    @Composable
    fun getCategoryName(nameResId: String): String {
        return when (nameResId) {
            "cogTest_market_category_frozen" -> stringResource(Res.string.cogTest_market_category_frozen)
            "cogTest_market_category_dairy" -> stringResource(Res.string.cogTest_market_category_dairy)
            "cogTest_market_category_fruits" -> stringResource(Res.string.cogTest_market_category_fruits)
            "cogTest_market_category_dry_spices" -> stringResource(Res.string.cogTest_market_category_dry_spices)
            "cogTest_market_category_vegetables" -> stringResource(Res.string.cogTest_market_category_vegetables)
            "cogTest_market_category_bakery" -> stringResource(Res.string.cogTest_market_category_bakery)
            "cogTest_market_category_meat" -> stringResource(Res.string.cogTest_market_category_meat)
            "cogTest_market_category_cleaning_disposable" -> stringResource(Res.string.cogTest_market_category_cleaning_disposable)
            else -> ""
        }
    }
}