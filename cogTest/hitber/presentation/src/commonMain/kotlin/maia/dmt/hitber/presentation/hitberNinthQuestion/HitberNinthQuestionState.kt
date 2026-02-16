package maia.dmt.hitber.presentation.hitberNinthQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberNinthQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
