package maia.dmt.auth.presentation.otp

import androidx.compose.foundation.text.input.TextFieldState
import maia.dmt.core.presentation.util.UiText

data class OtpState(
    val code: TextFieldState = TextFieldState(),
    val isVerifying: Boolean = false,
    val error: UiText? = null
)
