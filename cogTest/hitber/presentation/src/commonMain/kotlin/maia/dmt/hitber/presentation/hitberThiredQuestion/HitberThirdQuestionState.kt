package maia.dmt.hitber.presentation.hitberThiredQuestion

data class ReactionResult(
    val number: Int,
    val timeMs: Long,
)

data class HitberThirdQuestionState(
    val isPlaying: Boolean = false,
    val isFinished: Boolean = false,
    val currentNumber: Int? = null,
)
