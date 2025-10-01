package maia.dmt.auth.presentation.login

data class LoginState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)