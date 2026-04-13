package maia.dmt.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.auth.presentation.clinicselection.ClinicSelectionRoot
import maia.dmt.auth.presentation.login.LoginRoot
import maia.dmt.auth.presentation.otp.OtpRoot
import maia.dmt.auth.presentation.util.toJsonString


fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Login
    ) {
        composable<AuthGraphRoutes.Login> {
            LoginRoot(
                onLoginSuccess = { onLoginSuccess() },
                onTwoFactorRequired = { userId ->
                    navController.navigate(AuthGraphRoutes.Otp(userId = userId))
                },
                onClinicSelectionRequired = { userId, clinicsJson ->
                    navController.navigate(
                        AuthGraphRoutes.ClinicSelection(
                            userId = userId,
                            clinicsJson = clinicsJson
                        )
                    )
                }
            )
        }

        composable<AuthGraphRoutes.Otp> {
            OtpRoot(
                onSuccess = { onLoginSuccess() },
                onClinicSelectionRequired = { userId, clinics ->
                    val clinicsJson = clinics.toJsonString()
                    navController.navigate(
                        AuthGraphRoutes.ClinicSelection(
                            userId = userId,
                            clinicsJson = clinicsJson
                        )
                    )
                }
            )
        }

        composable<AuthGraphRoutes.ClinicSelection> {
            ClinicSelectionRoot(
                onSuccess = { onLoginSuccess() }
            )
        }
    }
}
