package maia.dmt.hitber.presentation.hitberFirstQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberFirstQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
