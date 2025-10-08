package maia.dmt.medication.presentation.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import maia.dmt.core.domain.auth.SessionStorage

class MedicationViewModel(
    private val sessionStorage: SessionStorage
): ViewModel() {

    private val _state = MutableStateFlow(MedicationState())

    private val eventChannel = Channel<MedicationEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false


    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MedicationState()
        )

    fun onAction(action: MedicationAction) {
        when (action) {
            MedicationAction.OnMedicationReportClick -> {}
            MedicationAction.OnMedicationReminderCLick -> {}
            MedicationAction.OnBackClick -> {}
            else -> {}
        }
    }



}