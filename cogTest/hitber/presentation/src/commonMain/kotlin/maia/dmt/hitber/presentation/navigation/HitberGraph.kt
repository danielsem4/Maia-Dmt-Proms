package maia.dmt.hitber.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import maia.dmt.hitber.presentation.hitberEightQuestion.HitberEightQuestionRoot
import maia.dmt.hitber.presentation.hitberEnd.HitberEndRoot
import maia.dmt.hitber.presentation.hitberEntry.HitberEntryRoot
import maia.dmt.hitber.presentation.hitberFifthQuestion.HitberFifthQuestionRoot
import maia.dmt.hitber.presentation.hitberFirstQuestion.HitberFirstQuestionRoot
import maia.dmt.hitber.presentation.hitberSecondQuestion.HitberSecondQuestionRoot
import maia.dmt.hitber.presentation.hitberShapeMemoryScreen.HitberShapeShowRoot
import maia.dmt.hitber.presentation.hitberFourthQuestion.HitberFourthQuestionRoot
import maia.dmt.hitber.presentation.hitberNinthQuestion.HitberNinthQuestionRoot
import maia.dmt.hitber.presentation.hitberSeventhQuestion.HitberSeventhQuestionRoot
import maia.dmt.hitber.presentation.hitberSixthQuestion.HitberSixthQuestionRoot
import maia.dmt.hitber.presentation.hitberTenthQuestion.HitberTenthQuestionRoot
import maia.dmt.hitber.presentation.hitberThiredQuestion.HitberThirdQuestionRoot

fun NavGraphBuilder.hitberTestGraph(
    navController: NavController,
) {
    navigation<HitberGraphRoutes.Graph>(
        startDestination = HitberGraphRoutes.HitberLand
    ) {
        composable<HitberGraphRoutes.HitberLand> {
            HitberEntryRoot(
                onNavigateToTest = { navController.navigate(HitberGraphRoutes.HitberFirstQuestion) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HitberGraphRoutes.HitberFirstQuestion> {
            HitberFirstQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberShapeShow) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HitberGraphRoutes.HitberShapeShow> {
            HitberShapeShowRoot(
                onNavigateNext = { navController.navigate(HitberGraphRoutes.HitberSecondQuestion) },
//                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HitberGraphRoutes.HitberSecondQuestion> {
            HitberSecondQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberThirdQuestion) },
            )
        }

        composable<HitberGraphRoutes.HitberThirdQuestion> {
            HitberThirdQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberFourthQuestion) },
            )
        }

        composable<HitberGraphRoutes.HitberFourthQuestion> {
            HitberFourthQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberSeventhQuestion) },
            )
        }

        composable<HitberGraphRoutes.HitberFifthQuestion> {
            HitberFifthQuestionRoot()
        }

        composable<HitberGraphRoutes.HitberSixthQuestion> {
            HitberSixthQuestionRoot()
        }

        composable<HitberGraphRoutes.HitberSeventhQuestion> {
            HitberSeventhQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberEighthQuestion) },
            )
        }

        composable<HitberGraphRoutes.HitberEighthQuestion> {
            HitberEightQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberNinthQuestion) },
            )
        }

        composable<HitberGraphRoutes.HitberNinthQuestion> {
            HitberNinthQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberTenthQuestion) },
            )
        }

        composable<HitberGraphRoutes.HitberTenthQuestion> {
            HitberTenthQuestionRoot(
                onNavigateToNextScreen = { navController.navigate(HitberGraphRoutes.HitberEnd) },
            )
        }

        composable<HitberGraphRoutes.HitberEnd> {
            HitberEndRoot(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}