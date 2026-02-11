package maia.dmt.pass.presentation.passEntry

import maia.dmt.core.domain.dto.evaluation.Evaluation

data class PassEntryState(
    val isPlayingAudio: Boolean = true,
    val evaluation: Evaluation? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)