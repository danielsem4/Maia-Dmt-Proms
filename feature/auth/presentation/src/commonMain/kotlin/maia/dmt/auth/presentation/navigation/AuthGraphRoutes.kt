package maia.dmt.auth.presentation.navigation

import kotlinx.serialization.Serializable

interface AuthGraphRoutes {

    @Serializable
    data object Graph: AuthGraphRoutes

    @Serializable
    data object Login: AuthGraphRoutes

    @Serializable
    data class Otp(val userId: String): AuthGraphRoutes

    @Serializable
    data class ClinicSelection(val userId: String, val clinicsJson: String): AuthGraphRoutes
}
