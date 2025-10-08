package maia.dmt.medication.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.medication.presentation.medications.MedicationsRoot

fun NavGraphBuilder.medicationGraph(
    navController: NavController
) {
    navigation<MedicationsGraphRoutes.Graph>(
        startDestination = MedicationsGraphRoutes.Medications
    ) {
        composable<MedicationsGraphRoutes.Medications> {
            MedicationsRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAllMedications = { isReport ->
                    navController.navigate(MedicationsGraphRoutes.AllMedications(isReport = isReport))
                }
            )
        }

        // Add your AllMedications screen here
        // composable<MedicationsGraphRoutes.AllMedications> { backStackEntry ->
        //     val args = backStackEntry.toRoute<MedicationsGraphRoutes.AllMedications>()
        //     AllMedicationsRoot(isReport = args.isReport)
        // }
    }
}