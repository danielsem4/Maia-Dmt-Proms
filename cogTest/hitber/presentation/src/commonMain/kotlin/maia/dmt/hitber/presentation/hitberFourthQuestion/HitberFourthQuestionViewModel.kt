package maia.dmt.hitber.presentation.hitberFourthQuestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import maia.dmt.hitber.presentation.session.HitberQ4Result
import maia.dmt.hitber.presentation.session.HitberSessionManager

class HitberFourthQuestionViewModel(
    private val sessionManager: HitberSessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(HitberFourthQuestionState())

    private val eventChannel = Channel<HitberFourthQuestionEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HitberFourthQuestionState()
        )

    private var firstCorrectWord: HitberWord = HitberWord.PENCIL
    private var secondCorrectWord: HitberWord = HitberWord.RULER
    private var firstImageUrl: String = ""
    private var secondImageUrl: String = ""
    private var step1SelectedWord: HitberWord? = null

    init {
        setupForVersion(sessionManager.sessionData.value.testVersion)
    }

    private fun setupForVersion(version: Int) {
        when (version) {
            0 -> {
                firstCorrectWord = HitberWord.PENCIL
                secondCorrectWord = HitberWord.RULER
                firstImageUrl = VERSION_1_PIC_1
                secondImageUrl = VERSION_1_PIC_2
            }
            1 -> {
                firstCorrectWord = HitberWord.TABLE
                secondCorrectWord = HitberWord.BALL
                firstImageUrl = VERSION_2_PIC_1
                secondImageUrl = VERSION_2_PIC_2
            }
            2 -> {
                firstCorrectWord = HitberWord.BALLOON
                secondCorrectWord = HitberWord.LEMON
                firstImageUrl = VERSION_3_PIC_1
                secondImageUrl = VERSION_3_PIC_2
            }
            3 -> {
                firstCorrectWord = HitberWord.LEMON
                secondCorrectWord = HitberWord.PENCIL
                firstImageUrl = VERSION_4_PIC_1
                secondImageUrl = VERSION_4_PIC_2
            }
        }

        _state.update {
            it.copy(
                currentImageUrl = firstImageUrl,
                options = buildOptions(firstCorrectWord),
            )
        }
    }

    private fun buildOptions(correctWord: HitberWord): List<HitberWord> {
        val distractors = HitberWord.entries
            .filter { it != correctWord }
            .shuffled()
            .take(DISTRACTOR_COUNT)
        return (listOf(correctWord) + distractors).shuffled()
    }

    fun onAction(action: HitberFourthQuestionAction) {
        when (action) {
            is HitberFourthQuestionAction.OnWordSelected -> {
                _state.update { it.copy(selectedWord = action.word) }
            }
            is HitberFourthQuestionAction.OnNextClick -> handleNext()
        }
    }

    private fun handleNext() {
        val selected = _state.value.selectedWord ?: return

        when (_state.value.currentStep) {
            QuestionStep.STEP_1 -> {
                step1SelectedWord = selected
                _state.update {
                    it.copy(
                        currentStep = QuestionStep.STEP_2,
                        currentImageUrl = secondImageUrl,
                        selectedWord = null,
                        options = buildOptions(secondCorrectWord),
                    )
                }
            }
            QuestionStep.STEP_2 -> {
                sessionManager.recordQ4Result(
                    HitberQ4Result(
                        originalWord1 = firstCorrectWord.key,
                        selectedWord1 = step1SelectedWord?.key ?: "",
                        originalWord2 = secondCorrectWord.key,
                        selectedWord2 = selected.key,
                    )
                )
                viewModelScope.launch {
                    eventChannel.send(HitberFourthQuestionEvent.NavigateToNextScreen)
                }
            }
        }
    }

    companion object {
        private const val DISTRACTOR_COUNT = 7

        private const val VERSION_1_PIC_1 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%941%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A8%D7%90%D7%A9%D7%95%D7%A0%D7%94.jpg?alt=media&token=1d064c87-9abf-4d45-926c-45a7829cd201"
        private const val VERSION_1_PIC_2 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%94%202%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A8%D7%90%D7%A9%D7%95%D7%A0%D7%94.png?alt=media&token=515f0963-a2b0-4798-9c1b-3995b09a5181"
        private const val VERSION_2_PIC_1 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%941%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A9%D7%A0%D7%99%D7%94.jpg?alt=media&token=7be08784-b28e-43f2-baf4-4afde2ce963c"
        private const val VERSION_2_PIC_2 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%942%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A9%D7%A0%D7%99%D7%94.jpg?alt=media&token=77bb33eb-899c-4264-8561-9fcda5dc4d9a"
        private const val VERSION_3_PIC_1 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%941%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A9%D7%9C%D7%99%D7%A9%D7%99%D7%AA.png?alt=media&token=10979082-663b-4739-a549-188a372c9128"
        private const val VERSION_3_PIC_2 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%942%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A9%D7%9C%D7%99%D7%A9%D7%99%D7%AA.jpg?alt=media&token=6d60dd4f-b49b-44e3-84b6-644dd2c6fee7"
        private const val VERSION_4_PIC_1 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%942%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A9%D7%9C%D7%99%D7%A9%D7%99%D7%AA.jpg?alt=media&token=6d60dd4f-b49b-44e3-84b6-644dd2c6fee7"
        private const val VERSION_4_PIC_2 =
            "https://firebasestorage.googleapis.com/v0/b/minimental-hit.appspot.com/o/FifthQuestion%20Pictures%2F%D7%AA%D7%9E%D7%95%D7%A0%D7%941%20%D7%92%D7%A8%D7%A1%D7%90%20%D7%A8%D7%90%D7%A9%D7%95%D7%A0%D7%94.jpg?alt=media&token=1d064c87-9abf-4d45-926c-45a7829cd201"
    }
}
