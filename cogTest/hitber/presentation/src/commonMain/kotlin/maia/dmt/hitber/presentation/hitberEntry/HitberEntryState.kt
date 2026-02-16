package maia.dmt.hitber.presentation.hitberEntry

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberEntryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
