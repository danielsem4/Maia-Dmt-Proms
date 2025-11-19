package maia.dmt.market.presentation.shoppingList

import dmtproms.cogtest.market.presentation.generated.resources.*
import org.jetbrains.compose.resources.StringResource

data class MarketShoppingListState(
    val listType: String = "regular",
    val groceries: List<GroceryItem> = emptyList(),
    val isLoading: Boolean = false,
)

data class GroceryItem(
    val stringRes: GroceryStringRes,
)

enum class GroceryStringRes(val resourceId: StringResource) {
    TomatoesKg(Res.string.cogTest_market_item_tomatoes_kg),
    CucumberHalfKg(Res.string.cogTest_market_item_cucumber_half_kg),
    BroccoliFresh(Res.string.cogTest_market_item_broccoli_fresh),

    BreadWholeLowSugar(Res.string.cogTest_market_item_bread_whole_low_sugar),
    GlutenFreeCookies(Res.string.cogTest_market_item_gluten_free_cookies),

    OliveOil(Res.string.cogTest_market_item_olive_oil),
    SunflowerOil(Res.string.cogTest_market_item_sunflower_oil),
    CornCans3(Res.string.cogTest_market_item_corn_cans_3),
    Pickles250(Res.string.cogTest_market_item_pickles_250),
    Tuna(Res.string.cogTest_market_item_tuna),

    SchnitzelKg(Res.string.cogTest_market_item_schnitzel_kg),
    ChickenLegs4(Res.string.cogTest_market_item_chicken_legs_4),

    Apricots10(Res.string.cogTest_market_item_apricots_10),
    Bananas700g(Res.string.cogTest_market_item_bananas_700g),
    Apples5Smith(Res.string.cogTest_market_item_apples_5_smith),
    Melon1(Res.string.cogTest_market_item_melon_1),

    Milk1Percent(Res.string.cogTest_market_item_milk_1_percent),
    CreamCheese28(Res.string.cogTest_market_item_cream_cheese_28),
    WhiteCheese250(Res.string.cogTest_market_item_white_cheese_250),
    Tzefatit5(Res.string.cogTest_market_item_tzefatit_5),

    Challah(Res.string.cogTest_market_donation_item_challah),
    TomatoPaste(Res.string.cogTest_market_donation_item_tomato_paste),
    CanolaOil(Res.string.cogTest_market_donation_item_canola_oil),
    Milk3Percent(Res.string.cogTest_market_donation_item_milk_3_percent),
    Olives(Res.string.cogTest_market_donation_item_olives),
    Chickpeas(Res.string.cogTest_market_donation_item_chickpeas),
    DisposableCups(Res.string.cogTest_market_donation_item_disposable_cups),
    DishSoap(Res.string.cogTest_market_donation_item_dish_soap),


}