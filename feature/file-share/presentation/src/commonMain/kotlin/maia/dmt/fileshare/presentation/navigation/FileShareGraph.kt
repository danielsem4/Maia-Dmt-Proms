package maia.dmt.fileshare.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import maia.dmt.fileshare.presentation.fileList.FileListRoot
import maia.dmt.fileshare.presentation.filePreview.FilePreviewRoot

fun NavGraphBuilder.fileShareGraph(
    navController: NavController
) {
    navigation<FileShareGraphRoutes.Graph>(
        startDestination = FileShareGraphRoutes.FileList
    ) {
        composable<FileShareGraphRoutes.FileList> {
            FileListRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToAddDocument = {
                    // TODO: navigate to add document screen
                },
                onNavigateToFilePreview = { fileId, fileName, fileType ->
                    navController.navigate(
                        FileShareGraphRoutes.FilePreview(
                            fileId = fileId,
                            fileName = fileName,
                            fileType = fileType
                        )
                    )
                }
            )
        }

        composable<FileShareGraphRoutes.FilePreview> { backStackEntry ->
            val args = backStackEntry.toRoute<FileShareGraphRoutes.FilePreview>()
            FilePreviewRoot(
                fileId = args.fileId,
                fileName = args.fileName,
                fileType = args.fileType,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
