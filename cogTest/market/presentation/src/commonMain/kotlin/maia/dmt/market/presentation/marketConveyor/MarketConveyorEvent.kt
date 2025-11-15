package maia.dmt.market.presentation.marketConveyor

interface MarketConveyorEvent {
    data object NavigateBack: MarketConveyorEvent

    data object NavigateToMarketSecondPart: MarketConveyorEvent

    data class PressGrocery(val grocery: String): MarketConveyorEvent

}