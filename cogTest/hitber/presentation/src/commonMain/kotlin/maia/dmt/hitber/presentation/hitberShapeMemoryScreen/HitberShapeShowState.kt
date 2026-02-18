package maia.dmt.hitber.presentation.hitberShapeMemoryScreen

import maia.dmt.hitber.domain.model.HitberShape

data class HitberShapeShowState(
    val selectedShapes: List<HitberShape> = emptyList(),
    val showInfoDialog: Boolean = true,
    val timerSeconds: Int = 30
)
