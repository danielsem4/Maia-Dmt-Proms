package maia.dmt.orientation.presentation.end

data class EndOrientationState(
    val isUploading: Boolean = false,
    val error: String? = null,
    val uploadSuccess: Boolean = false
)
