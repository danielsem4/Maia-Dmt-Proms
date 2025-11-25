package maia.dmt.market.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dmtproms.cogtest.market.presentation.generated.resources.*
import org.jetbrains.compose.resources.painterResource

object MarketProductImageMapper {

    @Composable
    fun getPainter(iconResId: String): Painter {
        return when (iconResId) {
            // Dairy
            "market_white_cheese" -> painterResource(Res.drawable.market_white_cheese)
            "market_milk1precent" -> painterResource(Res.drawable.market_milk1precent)
            "milk3precent" -> painterResource(Res.drawable.milk3precent)
            "market_milk_sweet_shamenet" -> painterResource(Res.drawable.market_milk_sweet_shamenet)
            "market_bulgarian_cheese" -> painterResource(Res.drawable.market_bulgarian_cheese)
            "market_yellow_cheese" -> painterResource(Res.drawable.market_yellow_cheese)
            "market_yellowcheese_megurad" -> painterResource(Res.drawable.market_yellowcheese_megurad)

            // Meat
            "market_meat_shnitzel" -> painterResource(Res.drawable.market_meat_shnitzel)
            "market_meat_chickenshok" -> painterResource(Res.drawable.market_meat_chickenshok)
            "market_meat_chickenbreast" -> painterResource(Res.drawable.market_meat_chickenbreast)
            "market_meat_pargit" -> painterResource(Res.drawable.market_meat_pargit)
            "market_meat_tahun" -> painterResource(Res.drawable.market_meat_tahun)

            // Vegetables
            "market_tomato" -> painterResource(Res.drawable.market_tomato)
            "market_vegetables_cherrytomato" -> painterResource(Res.drawable.market_vegetables_cherrytomato)
            "market_cucumber" -> painterResource(Res.drawable.market_cucumber)
            "market_broccoli" -> painterResource(Res.drawable.market_broccoli)
            "market_vegetables_carrot" -> painterResource(Res.drawable.market_vegetables_carrot)
            "market_vegetables_onion" -> painterResource(Res.drawable.market_vegetables_onion)
            "market_vegetables_cellery" -> painterResource(Res.drawable.market_vegetables_cellery)
            "market_lemon" -> painterResource(Res.drawable.market_lemon)

            // Fruits
            "market_fruits_mishmish" -> painterResource(Res.drawable.market_fruits_mishmish)
            "market_fruits_banana" -> painterResource(Res.drawable.market_fruits_banana)
            "market_fruits_peach" -> painterResource(Res.drawable.market_fruits_peach)
            "market_fruits_greenapple" -> painterResource(Res.drawable.market_fruits_greenapple)
            "market_fruits_redapple" -> painterResource(Res.drawable.market_fruits_redapple)
            "market_fruits_melon" -> painterResource(Res.drawable.market_fruits_melon)
            "market_fruits_grapes" -> painterResource(Res.drawable.market_fruits_grapes)

            // Frozen
            "market_frozen_brocoli" -> painterResource(Res.drawable.market_frozen_brocoli)
            "market_frozen_afuna" -> painterResource(Res.drawable.market_frozen_afuna)
            "market_frozen_carrotafuna" -> painterResource(Res.drawable.market_frozen_carrotafuna)
            "market_frozen_beans" -> painterResource(Res.drawable.market_frozen_beans)
            "market_frozen_borekascheese" -> painterResource(Res.drawable.market_frozen_borekascheese)
            "market_frozen_borekastapha" -> painterResource(Res.drawable.market_frozen_borekastapha)
            "market_frozen_jahnun" -> painterResource(Res.drawable.market_frozen_jahnun)

            // Dry & Spices
            "market_dry_cannedcorn" -> painterResource(Res.drawable.market_dry_cannedcorn)
            "market_dry_pickle" -> painterResource(Res.drawable.market_dry_pickle)
            "market_dry_tuna" -> painterResource(Res.drawable.market_dry_tuna)
            "market_dry_littlecorn" -> painterResource(Res.drawable.market_dry_littlecorn)
            "market_dry_sardine" -> painterResource(Res.drawable.market_dry_sardine)
            "market_kanola_oil" -> painterResource(Res.drawable.market_kanola_oil)
            "market_dry_oilsoya" -> painterResource(Res.drawable.market_dry_oilsoya)
            "market_olive_oil" -> painterResource(Res.drawable.market_olive_oil)
            "market_olives" -> painterResource(Res.drawable.market_olives)
            "market_salt" -> painterResource(Res.drawable.market_salt)
            "market_suger" -> painterResource(Res.drawable.market_suger)
            "market_black_pepper" -> painterResource(Res.drawable.market_black_pepper)
            "market_flour" -> painterResource(Res.drawable.market_flour)
            "market_cacao" -> painterResource(Res.drawable.market_cacao)
            "market_baking_powder" -> painterResource(Res.drawable.market_baking_powder)
            "market_vanil" -> painterResource(Res.drawable.market_vanil)
            "market_dry_tomatopaste" -> painterResource(Res.drawable.market_dry_tomatopaste)
            "market_dry_hummus" -> painterResource(Res.drawable.market_dry_hummus)

            // Bakery
            "market_bakery_nosuger_bread" -> painterResource(Res.drawable.market_bakery_nosuger_bread)
            "market_bakery_bread" -> painterResource(Res.drawable.market_bakery_bread)
            "market_bakery_halla" -> painterResource(Res.drawable.market_bakery_halla)
            "market_bakery_cookies" -> painterResource(Res.drawable.market_bakery_cookies)
            "market_bakery_cake" -> painterResource(Res.drawable.market_bakery_cake)

            // Cleaning
            "market_clean_economica" -> painterResource(Res.drawable.market_clean_economica)
            "market_clean_gloves" -> painterResource(Res.drawable.market_clean_gloves)
            "market_clean_dishsabon" -> painterResource(Res.drawable.market_clean_dishsabon)
            "market_clean_forwindows" -> painterResource(Res.drawable.market_clean_forwindows)
            "market_clean_garbagebag" -> painterResource(Res.drawable.market_clean_garbagebag)
            "market_clean_scotch" -> painterResource(Res.drawable.market_clean_scotch)
            "market_clean_cups" -> painterResource(Res.drawable.market_clean_cups)

            else -> painterResource(Res.drawable.market_white_cheese)
        }
    }
}