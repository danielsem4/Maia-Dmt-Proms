package maia.dmt.hitber.presentation.hitberSixthQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberSixthQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
