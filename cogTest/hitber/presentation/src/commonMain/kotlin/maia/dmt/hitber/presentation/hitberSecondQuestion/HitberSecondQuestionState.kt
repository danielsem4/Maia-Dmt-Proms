package maia.dmt.hitber.presentation.hitberSecondQuestion

import maia.dmt.hitber.domain.model.HitberShape

data class HitberSecondQuestionState(
    val visibleShapes: List<HitberShape> = HitberShape.entries.toList(),
    val selectedShapes: Set<HitberShape> = emptySet(),
    val attemptNumber: Int = 1,
    val showErrorDialog: Boolean = false,
)
