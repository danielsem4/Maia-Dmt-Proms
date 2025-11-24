package maia.dmt.market.presentation.shoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_canola_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_challah
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_chickpeas
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_dish_soap
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_disposable_cups
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_milk_3_percent
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_olives
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_donation_item_tomato_paste
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_apples_5_smith
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_apricots_10
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_bananas_700g
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_bread_whole_low_sugar
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_broccoli_fresh_out
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_chicken_legs_4
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_corn_cans_3
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cream_cheese_28
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_cucumber_half_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_gluten_free_cookies
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_melon_1
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_milk_1_percent
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_olive_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_pickles_250
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_schnitzel_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_sunflower_oil
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tomatoes_kg
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tuna_can
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_tzefatit_5
import dmtproms.cogtest.market.presentation.generated.resources.cogTest_market_item_white_cheese_250
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class MarketShoppingListViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarketShoppingListState())
    private var hasLoadedInitialData = false

    private val eventChannel = Channel<MarketShoppingListEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = _state.value
        )

    fun initialize(listType: String) {
        _state.update { it.copy(listType = listType) }
        setGroceries(listType)
    }

    fun onAction(action: MarketShoppingListAction) {
        when (action) {
            is MarketShoppingListAction.OnNavigateBack -> {
                navigateBack()
            }
        }
    }

    private fun setGroceries(listType: String) {
        _state.update { it.copy(isLoading = true) }

        val groceries = when (listType) {
            "regular" -> getRegularGroceries()
            "donation" -> getDonationGroceries()
            else -> emptyList()
        }

        _state.update {
            it.copy(
                groceries = groceries,
                isLoading = false
            )
        }
    }

    private fun getRegularGroceries(): List<StringResource> {
        return listOf(
            Res.string.cogTest_market_item_tomatoes_kg,
            Res.string.cogTest_market_item_broccoli_fresh_out,
            Res.string.cogTest_market_item_gluten_free_cookies,
            Res.string.cogTest_market_item_sunflower_oil,
            Res.string.cogTest_market_item_pickles_250,
            Res.string.cogTest_market_item_schnitzel_kg,
            Res.string.cogTest_market_item_apricots_10,
            Res.string.cogTest_market_item_apples_5_smith,
            Res.string.cogTest_market_item_milk_1_percent,
            Res.string.cogTest_market_item_white_cheese_250,
            Res.string.cogTest_market_item_cucumber_half_kg,
            Res.string.cogTest_market_item_bread_whole_low_sugar,
            Res.string.cogTest_market_item_olive_oil,
            Res.string.cogTest_market_item_corn_cans_3,
            Res.string.cogTest_market_item_tuna_can,
            Res.string.cogTest_market_item_chicken_legs_4,
            Res.string.cogTest_market_item_bananas_700g,
            Res.string.cogTest_market_item_melon_1,
            Res.string.cogTest_market_item_cream_cheese_28,
            Res.string.cogTest_market_item_tzefatit_5,
        )
    }

    private fun getDonationGroceries(): List<StringResource> {
        return listOf(
            Res.string.cogTest_market_donation_item_challah,
            Res.string.cogTest_market_donation_item_tomato_paste,
            Res.string.cogTest_market_donation_item_canola_oil,
            Res.string.cogTest_market_donation_item_milk_3_percent,
            Res.string.cogTest_market_donation_item_olives,
            Res.string.cogTest_market_donation_item_chickpeas,
            Res.string.cogTest_market_donation_item_disposable_cups,
            Res.string.cogTest_market_donation_item_dish_soap,
        )
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(MarketShoppingListEvent.NavigateBack)
        }
    }
}