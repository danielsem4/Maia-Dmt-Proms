package maia.dmt.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.settings.presentation.appearance.AppearanceRoot
import maia.dmt.settings.presentation.language.SettingsLanguageRoot
import maia.dmt.settings.presentation.language.SettingsLanguageScreen
import maia.dmt.settings.presentation.profile.ProfileRoot
import maia.dmt.settings.presentation.settings.SettingsRoot

fun NavGraphBuilder.settingsGraph(
    navController: NavController,
) {
    navigation<SettingsGraphRoutes.Graph>(
        startDestination = SettingsGraphRoutes.Settings
    ) {
        composable<SettingsGraphRoutes.Settings> {
            SettingsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToLanguage = {
                    navController.navigate(SettingsGraphRoutes.Language)
                },
                onNavigateToProfile = {
                    navController.navigate(SettingsGraphRoutes.Profile)
                },
                onNavigateToAppearance = {
                    navController.navigate(SettingsGraphRoutes.Appearance)
                }
            )
        }
        composable<SettingsGraphRoutes.Language> {
            SettingsLanguageRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<SettingsGraphRoutes.Profile> {
            ProfileRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<SettingsGraphRoutes.Appearance> {
            AppearanceRoot(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

    }
}