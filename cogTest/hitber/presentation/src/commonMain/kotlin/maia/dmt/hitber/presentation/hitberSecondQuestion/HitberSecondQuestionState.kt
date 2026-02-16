package maia.dmt.hitber.presentation.hitberSecondQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberSecondQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
