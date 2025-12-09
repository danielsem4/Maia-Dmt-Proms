package maia.dmt.cdt.presentation.cdtGrade

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.cdt.presentation.session.CdtSessionManager
import maia.dmt.cdt.presentation.util.CdtGradeResourceProvider

class CdtGradeViewModel(
    private val cdtSessionManager: CdtSessionManager,
    private val resourceProvider: CdtGradeResourceProvider
) : ViewModel() {

    private val _state = MutableStateFlow(CdtGradeState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = CdtGradeState()
    )

    private val eventChannel = Channel<CdtGradeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val circleGrades = resourceProvider.getCircleGrades()
            val numbersGrades = resourceProvider.getNumbersGrades()
            val handsGrades = resourceProvider.getHandsGrades()

            val drawing = getClockDrawing()

            val currentGrades = cdtSessionManager.grades.value

            _state.update {
                it.copy(
                    circleOptions = circleGrades,
                    numbersOptions = numbersGrades,
                    handsOptions = handsGrades,
                    clockBitmap = drawing,
                    selectedCircleGrade = currentGrades.circle.ifEmpty { null },
                    selectedNumbersGrade = currentGrades.numbers.ifEmpty { null },
                    selectedHandsGrade = currentGrades.hands.ifEmpty { null }
                )
            }
        }
    }

    private fun getClockDrawing(): ImageBitmap? {
        val drawings = cdtSessionManager.getAllDrawingBitmaps()
        val evaluation = cdtSessionManager.evaluation.value

        val mainIndex = evaluation?.measurement_objects?.indexOfFirst { it.object_label == "imageUrl" } ?: -1

        return if (mainIndex != -1 && drawings.containsKey(mainIndex)) {
            drawings[mainIndex]
        } else {
            drawings[0]
        }
    }

    fun onAction(action: CdtGradeAction) {
        when (action) {
            is CdtGradeAction.OnCircleGradeSelected -> {
                _state.update { it.copy(selectedCircleGrade = action.grade) }
            }
            is CdtGradeAction.OnNumbersGradeSelected -> {
                _state.update { it.copy(selectedNumbersGrade = action.grade) }
            }
            is CdtGradeAction.OnHandsGradeSelected -> {
                _state.update { it.copy(selectedHandsGrade = action.grade) }
            }
            is CdtGradeAction.OnSaveClick -> saveAndExit()
        }
    }

    private fun saveAndExit() {
        val s = _state.value
        cdtSessionManager.saveGrades(
            circle = s.selectedCircleGrade ?: "",
            numbers = s.selectedNumbersGrade ?: "",
            hands = s.selectedHandsGrade ?: ""
        )
        viewModelScope.launch {
            eventChannel.send(CdtGradeEvent.NavigateBack)
        }
    }
}