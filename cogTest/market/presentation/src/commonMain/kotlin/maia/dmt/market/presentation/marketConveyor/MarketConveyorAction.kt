package maia.dmt.market.presentation.marketConveyor

interface MarketConveyorAction {
    data object OnNavigateBack : MarketConveyorAction

    data object OnFinishFirstPart : MarketConveyorAction

    data class OnSelectGrocery(val grocery: String): MarketConveyorAction

}