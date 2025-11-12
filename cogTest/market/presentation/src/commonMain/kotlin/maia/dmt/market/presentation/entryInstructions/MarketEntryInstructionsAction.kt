package maia.dmt.market.presentation.entryInstructions

sealed interface MarketEntryInstructionsAction {
    data object OnNavigateBack : MarketEntryInstructionsAction

    data object OnStartMarketTest : MarketEntryInstructionsAction
}