package maia.dmt.core.domain.dto

sealed interface AuthResult {
    data class Authenticated(val tokens: AuthTokens, val user: User) : AuthResult
    data class TwoFactorRequired(val userId: String) : AuthResult
    data class ClinicSelectionRequired(val userId: String, val clinics: List<Clinic>) : AuthResult
}
