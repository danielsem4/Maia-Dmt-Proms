package maia.dmt.orientation.presentation.painValue

import kotlin.time.Instant

data class PainScaleOrientationState(
    val painLevel: Int = 0,
    val hasSetPainLevel: Boolean = false,
    val isPlayingAudio: Boolean = false,

    val startTime: Instant? = null,
    val firstInteractionTime: Instant? = null,
    val showInactivityDialog: Boolean = false,
    val inactivityTimeoutCount: Int = 0
)

enum class PainLevelCategory {
    VERY_GOOD,
    OK,
    BAD,
    VERY_BAD
}

fun Int.toPainLevelCategory(): PainLevelCategory {
    return when (this) {
        in 0..1 -> PainLevelCategory.VERY_GOOD
        in 2..3 -> PainLevelCategory.OK
        in 4..5 -> PainLevelCategory.BAD
        else -> PainLevelCategory.VERY_BAD
    }
}