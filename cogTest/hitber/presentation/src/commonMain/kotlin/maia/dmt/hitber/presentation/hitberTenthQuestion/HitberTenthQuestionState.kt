package maia.dmt.hitber.presentation.hitberTenthQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberTenthQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
