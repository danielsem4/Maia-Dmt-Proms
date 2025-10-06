package maia.dmt.auth.presentation.navigation

import kotlinx.serialization.Serializable

interface AuthGraphRoutes {

    @Serializable
    data object Graph: AuthGraphRoutes

    @Serializable
    data object Login: AuthGraphRoutes

    /**
     * Will soon
     *     @Serializable
     *     data object ForgotPassword: AuthGraphRoutes
     */


}