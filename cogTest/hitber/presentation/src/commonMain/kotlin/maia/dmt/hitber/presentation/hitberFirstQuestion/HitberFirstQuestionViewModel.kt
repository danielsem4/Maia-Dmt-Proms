package maia.dmt.hitber.presentation.hitberFirstQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberFirstQuestionViewModel(
    private val hitberSessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberFirstQuestionState())

    private val eventChannel = Channel<HitberFirstQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HitberFirstQuestionState()
        )

    init {
        loadQuestions()
    }

    fun onAction(action: HitberFirstQuestionAction) {
        when (action) {
            is HitberFirstQuestionAction.OnNextClick -> handleNextClick()
            is HitberFirstQuestionAction.OnBackClick -> navigateBack()
            is HitberFirstQuestionAction.OnAnswerSelected -> {
                _state.update {
                    it.copy(answers = it.answers + (action.questionId to action.answer))
                }
            }
        }
    }

    private fun loadQuestions() {
        val evaluation = hitberSessionManager.evaluation.value ?: return
        val targetIds = BATCH_IDS.flatten().toSet()

        val filteredQuestions = evaluation.evaluation_objects
            .filter { it.id in targetIds }

        _state.update {
            it.copy(questions = filteredQuestions)
        }
    }

    fun getCurrentBatchQuestions(): List<maia.dmt.core.domain.dto.evaluation.EvaluationObject> {
        val currentBatch = _state.value.currentBatchIndex
        if (currentBatch !in BATCH_IDS.indices) return emptyList()

        val batchIds = BATCH_IDS[currentBatch]
        val questions = _state.value.questions

        return batchIds.mapNotNull { id -> questions.find { it.id == id } }
    }

    private fun handleNextClick() {
        val currentBatch = _state.value.currentBatchIndex
        val batchIds = BATCH_IDS.getOrNull(currentBatch) ?: return
        val answers = _state.value.answers

        val unansweredIds = batchIds.filter { id ->
            val answer = answers[id]
            answer.isNullOrBlank()
        }

        if (unansweredIds.isEmpty()) {
            advanceBatch()
            return
        }

        if (!_state.value.hasAttemptedCurrentBatch) {
            viewModelScope.launch {
                eventChannel.send(HitberFirstQuestionEvent.ShowToast(""))
            }
            _state.update { it.copy(hasAttemptedCurrentBatch = true) }
        } else {
            val filledAnswers = _state.value.answers.toMutableMap()
            unansweredIds.forEach { id ->
                filledAnswers[id] = NO_ANSWER
            }
            _state.update { it.copy(answers = filledAnswers) }
            advanceBatch()
        }
    }

    private fun advanceBatch() {
        val currentBatch = _state.value.currentBatchIndex

        if (currentBatch < BATCH_IDS.size - 1) {
            _state.update {
                it.copy(
                    currentBatchIndex = currentBatch + 1,
                    hasAttemptedCurrentBatch = false
                )
            }
        } else {
            syncAnswersToSession()
            viewModelScope.launch {
                eventChannel.send(HitberFirstQuestionEvent.NavigateToNextScreen)
            }
        }
    }

    private fun syncAnswersToSession() {
        val evaluation = hitberSessionManager.evaluation.value ?: return
        val answers = _state.value.answers

        val updatedObjects = evaluation.evaluation_objects.map { obj ->
            val answer = answers[obj.id]
            if (answer != null) obj.copy(answer = answer) else obj
        }

        val updatedEvaluation = evaluation.copy(evaluation_objects = updatedObjects)
        hitberSessionManager.setEvaluation(updatedEvaluation)
    }

    private fun navigateBack() {
        viewModelScope.launch {
            eventChannel.send(HitberFirstQuestionEvent.NavigateBack)
        }
    }

    companion object {
        private val BATCH_IDS = listOf(
            listOf(101, 102, 109),
            listOf(104, 105, 106),
            listOf(329, 330, 108)
        )
        private const val NO_ANSWER = "No Answer"
    }
}
