package maia.dmt.orientation.presentation.entry

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class EntryOrientationState(
    val evaluation: Evaluation? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
