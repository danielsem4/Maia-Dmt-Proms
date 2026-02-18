package maia.dmt.hitber.presentation.session

import maia.dmt.hitber.domain.model.HitberShape

data class HitberSessionData(
    val targetShapes: List<HitberShape> = emptyList(),
    val q2Attempts: List<HitberQ2Attempt> = emptyList(),
)

data class HitberQ2Attempt(
    val attemptNumber: Int,
    val selectedShapes: List<HitberShape>,
    val wrongShapeCount: Int,
    val isSuccess: Boolean,
)
