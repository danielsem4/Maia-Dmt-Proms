package maia.dmt.hitber.presentation.hitberThiredQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberThiredQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
