package maia.dmt.settings.presentation.navigation

import kotlinx.serialization.Serializable

interface SettingsGraphRoutes {

    @Serializable
    data object Graph: SettingsGraphRoutes

    @Serializable
    data object Settings: SettingsGraphRoutes

}