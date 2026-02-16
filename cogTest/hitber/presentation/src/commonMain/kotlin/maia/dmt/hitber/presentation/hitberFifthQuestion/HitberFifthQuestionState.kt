package maia.dmt.hitber.presentation.hitberFifthQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberFifthQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
