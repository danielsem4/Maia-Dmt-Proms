package maia.dmt.auth.presentation.otp

sealed interface OtpAction {
    data object OnVerifyClick : OtpAction
    data object OnResendClick : OtpAction
}
