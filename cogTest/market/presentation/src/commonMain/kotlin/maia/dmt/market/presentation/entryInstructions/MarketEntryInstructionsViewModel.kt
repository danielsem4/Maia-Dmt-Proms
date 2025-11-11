package maia.dmt.market.presentation.entryInstructions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MarketEntryInstructionsViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarketEntryInstructionsState())
    val state = _state.asStateFlow()
}