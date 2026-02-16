package maia.dmt.hitber.presentation.hitberEightQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberEightQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
