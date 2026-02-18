package maia.dmt.hitber.presentation.hitberFourthQuestion

enum class QuestionStep { STEP_1, STEP_2 }

enum class HitberWord(val key: String) {
    PENCIL("pencil"),
    RULER("ruler"),
    TABLE("table"),
    BALL("ball"),
    BALLOON("balloon"),
    LEMON("lemon"),
    KEY("key"),
    WATCH("watch"),
    GLASSES("glasses"),
    SHOE("shoe"),
    PHONE("phone"),
    BOOK("book"),
}

data class HitberFourthQuestionState(
    val currentStep: QuestionStep = QuestionStep.STEP_1,
    val currentImageUrl: String = "",
    val selectedWord: HitberWord? = null,
    val options: List<HitberWord> = emptyList(),
)
