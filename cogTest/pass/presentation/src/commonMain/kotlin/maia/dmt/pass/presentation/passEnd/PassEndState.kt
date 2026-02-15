package maia.dmt.pass.presentation.passEnd

data class PassEndState(
    val isUploading: Boolean = false,
    val error: String? = null,
    val uploadSuccess: Boolean = false,
    val isPlayingAudio: Boolean = true
)
