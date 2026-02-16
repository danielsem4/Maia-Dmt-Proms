package maia.dmt.hitber.presentation.hitberFourthQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberFourthQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
