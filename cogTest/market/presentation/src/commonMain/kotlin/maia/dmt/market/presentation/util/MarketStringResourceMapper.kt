package maia.dmt.market.presentation.util

import androidx.compose.runtime.Composable
import dmtproms.cogtest.market.presentation.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

object MarketStringResourceMapper {

    /**
     * Helper for ViewModel: Returns the Resource ID object (Non-Composable)
     */
    fun getProductResource(titleResId: String): StringResource {
        return when (titleResId) {
            // Dairy
            "cogTest_market_item_white_cheese_500" -> Res.string.cogTest_market_item_white_cheese_500
            "cogTest_market_item_white_cheese_250" -> Res.string.cogTest_market_item_white_cheese_250
            "cogTest_market_item_milk_1_liter_1_percent" -> Res.string.cogTest_market_item_milk_1_liter_1_percent
            "cogTest_market_item_milk_1_liter_3_percent" -> Res.string.cogTest_market_item_milk_1_liter_3_percent
            "cogTest_market_item_sweet_cream_38" -> Res.string.cogTest_market_item_sweet_cream_38
            "cogTest_market_item_bulgarian_250" -> Res.string.cogTest_market_item_bulgarian_250
            "cogTest_market_item_yellow_sliced_250" -> Res.string.cogTest_market_item_yellow_sliced_250
            "cogTest_market_item_yellow_grated_500" -> Res.string.cogTest_market_item_yellow_grated_500

            // Meat
            "cogTest_market_item_shnitzel_kg" -> Res.string.cogTest_market_item_shnitzel_kg
            "cogTest_market_item_legs_4" -> Res.string.cogTest_market_item_legs_4
            "cogTest_market_item_breast_kg" -> Res.string.cogTest_market_item_breast_kg
            "cogTest_market_item_pargit" -> Res.string.cogTest_market_item_pargit
            "cogTest_market_item_beef_ground_500" -> Res.string.cogTest_market_item_beef_ground_500

            // Vegetables
            "cogTest_market_item_tomato_half_kg" -> Res.string.cogTest_market_item_tomato_half_kg
            "cogTest_market_item_cherry_tomato" -> Res.string.cogTest_market_item_cherry_tomato
            "cogTest_market_item_cucumber_half_kg" -> Res.string.cogTest_market_item_cucumber_half_kg
            "cogTest_market_item_broccoli_fresh_out" -> Res.string.cogTest_market_item_broccoli_fresh_out
            "cogTest_market_item_carrot_250" -> Res.string.cogTest_market_item_carrot_250
            "cogTest_market_item_onion" -> Res.string.cogTest_market_item_onion
            "cogTest_market_item_celery" -> Res.string.cogTest_market_item_celery
            "cogTest_market_item_lemon" -> Res.string.cogTest_market_item_lemon

            // Fruits
            "cogTest_market_item_apricot_pack" -> Res.string.cogTest_market_item_apricot_pack
            "cogTest_market_item_banana_100g" -> Res.string.cogTest_market_item_banana_100g
            "cogTest_market_item_nectarine" -> Res.string.cogTest_market_item_nectarine
            "cogTest_market_item_green_apple" -> Res.string.cogTest_market_item_green_apple
            "cogTest_market_item_red_apple" -> Res.string.cogTest_market_item_red_apple
            "cogTest_market_item_melon" -> Res.string.cogTest_market_item_melon
            "cogTest_market_item_grapes_500" -> Res.string.cogTest_market_item_grapes_500

            // Frozen
            "cogTest_market_item_broccoli_frozen" -> Res.string.cogTest_market_item_broccoli_frozen
            "cogTest_market_item_peas_frozen" -> Res.string.cogTest_market_item_peas_frozen
            "cogTest_market_item_pea_carrot_mix" -> Res.string.cogTest_market_item_pea_carrot_mix
            "cogTest_market_item_cauliflower_frozen" -> Res.string.cogTest_market_item_cauliflower_frozen
            "cogTest_market_item_green_beans_frozen" -> Res.string.cogTest_market_item_green_beans_frozen
            "cogTest_market_item_borekas_cheese" -> Res.string.cogTest_market_item_borekas_cheese
            "cogTest_market_item_borekas_potato" -> Res.string.cogTest_market_item_borekas_potato
            "cogTest_market_item_jahnun" -> Res.string.cogTest_market_item_jahnun

            // Dry & Spices
            "cogTest_market_item_canned_corn" -> Res.string.cogTest_market_item_canned_corn
            "cogTest_market_item_pickles_vinegar_250" -> Res.string.cogTest_market_item_pickles_vinegar_250
            "cogTest_market_item_pickles_salt_250" -> Res.string.cogTest_market_item_pickles_salt_250
            "cogTest_market_item_tuna_can" -> Res.string.cogTest_market_item_tuna_can
            "cogTest_market_item_baby_corn" -> Res.string.cogTest_market_item_baby_corn
            "cogTest_market_item_sardines" -> Res.string.cogTest_market_item_sardines
            "cogTest_market_item_sunflower_oil" -> Res.string.cogTest_market_item_sunflower_oil
            "cogTest_market_item_soy_oil" -> Res.string.cogTest_market_item_soy_oil
            "cogTest_market_item_coconut_oil" -> Res.string.cogTest_market_item_coconut_oil
            "cogTest_market_item_olives_canned" -> Res.string.cogTest_market_item_olives_canned
            "cogTest_market_item_salt_basic" -> Res.string.cogTest_market_item_salt_basic
            "cogTest_market_item_sugar_basic" -> Res.string.cogTest_market_item_sugar_basic
            "cogTest_market_item_black_pepper_basic" -> Res.string.cogTest_market_item_black_pepper_basic
            "cogTest_market_item_flour_basic" -> Res.string.cogTest_market_item_flour_basic
            "cogTest_market_item_cacao_basic" -> Res.string.cogTest_market_item_cacao_basic
            "cogTest_market_item_baking_powder_basic" -> Res.string.cogTest_market_item_baking_powder_basic
            "cogTest_market_item_vanilla_extract_basic" -> Res.string.cogTest_market_item_vanilla_extract_basic
            "cogTest_market_item_tomato_paste" -> Res.string.cogTest_market_item_tomato_paste
            "cogTest_market_item_hummus_dry" -> Res.string.cogTest_market_item_hummus_dry

            // Bakery
            "cogTest_market_item_bread_no_sugar" -> Res.string.cogTest_market_item_bread_no_sugar
            "cogTest_market_item_bread_regular" -> Res.string.cogTest_market_item_bread_regular
            "cogTest_market_item_challah" -> Res.string.cogTest_market_item_challah
            "cogTest_market_item_cookies_gluten_free" -> Res.string.cogTest_market_item_cookies_gluten_free
            "cogTest_market_item_yeast_cake" -> Res.string.cogTest_market_item_yeast_cake

            // Cleaning
            "cogTest_market_item_bleach" -> Res.string.cogTest_market_item_bleach
            "cogTest_market_item_gloves" -> Res.string.cogTest_market_item_gloves
            "cogTest_market_item_dish_soap" -> Res.string.cogTest_market_item_dish_soap
            "cogTest_market_item_window_cleaner" -> Res.string.cogTest_market_item_window_cleaner
            "cogTest_market_item_garbage_bags" -> Res.string.cogTest_market_item_garbage_bags
            "cogTest_market_item_scotch_pack" -> Res.string.cogTest_market_item_scotch_pack
            "cogTest_market_item_disposable_cups_pack" -> Res.string.cogTest_market_item_disposable_cups_pack

            // Fallback (since we can't return the raw string anymore, we return a safe default)
            else -> Res.string.cogTest_market_item_white_cheese_500
        }
    }

    /**
     * Helper for UI: Resolves the String immediately (Composable)
     * Used by DmtGroceryItemMenuCard, etc.
     */
    @Composable
    fun getProductName(titleResId: String): String {
        return stringResource(getProductResource(titleResId))
    }


    /**
     * Helper for ViewModel: Returns the Resource ID object (Non-Composable)
     */
    fun getCategoryResource(nameResId: String): StringResource {
        return when (nameResId) {
            "cogTest_market_category_frozen" -> Res.string.cogTest_market_category_frozen
            "cogTest_market_category_dairy" -> Res.string.cogTest_market_category_dairy
            "cogTest_market_category_fruits" -> Res.string.cogTest_market_category_fruits
            "cogTest_market_category_dry_spices" -> Res.string.cogTest_market_category_dry_spices
            "cogTest_market_category_vegetables" -> Res.string.cogTest_market_category_vegetables
            "cogTest_market_category_bakery" -> Res.string.cogTest_market_category_bakery
            "cogTest_market_category_meat" -> Res.string.cogTest_market_category_meat
            "cogTest_market_category_cleaning_disposable" -> Res.string.cogTest_market_category_cleaning_disposable
            else -> Res.string.cogTest_market_category_dairy // Fallback
        }
    }

    /**
     * Helper for UI: Resolves the String immediately (Composable)
     */
    @Composable
    fun getCategoryName(nameResId: String): String {
        return stringResource(getCategoryResource(nameResId))
    }
}