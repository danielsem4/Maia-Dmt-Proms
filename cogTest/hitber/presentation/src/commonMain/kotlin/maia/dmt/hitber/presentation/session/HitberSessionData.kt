package maia.dmt.hitber.presentation.session

import maia.dmt.hitber.domain.model.HitberShape

data class HitberSessionData(
    val testVersion: Int = 0,
    val targetShapes: List<HitberShape> = emptyList(),
    val q2Attempts: List<HitberQ2Attempt> = emptyList(),
    val q3Result: HitberQ3Result? = null,
    val q4Result: HitberQ4Result? = null,
    val q8Result: HitberQ8Result? = null,
    val q9Result: HitberQ9Result? = null,
)

data class HitberQ9Result(
    val constructedSentence: String,
    val correctSentence: String,
    val isCorrect: Boolean,
)

data class HitberQ8Result(
    val targetBallColor: String,
    val droppedBallColor: String,
    val isCorrect: Boolean,
)

data class HitberQ2Attempt(
    val attemptNumber: Int,
    val selectedShapes: List<HitberShape>,
    val wrongShapeCount: Int,
    val isSuccess: Boolean,
)

data class HitberQ3Result(
    val numberSequence: List<Int>,
    val reactions: List<HitberQ3Reaction>,
)

data class HitberQ3Reaction(
    val number: Int,
    val timeMs: Long,
)

data class HitberQ4Result(
    val originalWord1: String,
    val selectedWord1: String,
    val originalWord2: String,
    val selectedWord2: String,
)
