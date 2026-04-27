package maia.dmt.fileshare.presentation.navigation

import kotlinx.serialization.Serializable

interface FileShareGraphRoutes {

    @Serializable
    data object Graph : FileShareGraphRoutes

    @Serializable
    data object FileList : FileShareGraphRoutes

    @Serializable
    data class FilePreview(val fileId: String, val fileName: String, val fileType: String) : FileShareGraphRoutes
}
