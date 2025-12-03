package maia.dmt.cdt.presentation.cdtLand

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class CdtLandState(
    val isLoading: Boolean = false,
    val evaluation: Evaluation? = null,
    val error: String? = null
)
