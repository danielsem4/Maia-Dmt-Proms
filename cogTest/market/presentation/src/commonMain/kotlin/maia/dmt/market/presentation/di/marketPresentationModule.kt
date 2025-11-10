package maia.dmt.market.presentation.di

import maia.dmt.market.presentation.entryInstructions.MarketEntryInstructionsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val marketPresentationModule = module {
    viewModelOf(::MarketEntryInstructionsViewModel)
}