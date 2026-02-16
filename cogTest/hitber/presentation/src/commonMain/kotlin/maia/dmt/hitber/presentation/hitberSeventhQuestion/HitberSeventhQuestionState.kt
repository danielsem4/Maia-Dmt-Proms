package maia.dmt.hitber.presentation.hitberSeventhQuestion

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class HitberSeventhQuestionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
)
