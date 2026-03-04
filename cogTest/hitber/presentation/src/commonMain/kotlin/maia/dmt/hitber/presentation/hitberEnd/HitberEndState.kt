package maia.dmt.hitber.presentation.hitberEnd

import maia.dmt.core.domain.dto.evaluation.Evaluation
import maia.dmt.hitber.presentation.session.HitberSessionData

data class HitberEndState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val evaluation: Evaluation? = null,
    val sessionData: HitberSessionData = HitberSessionData(),
)
