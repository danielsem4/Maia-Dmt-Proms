package maia.dmt.fileshare.presentation.di

import maia.dmt.fileshare.presentation.fileList.FileListViewModel
import maia.dmt.fileshare.presentation.filePreview.FilePreviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val fileSharePresentationModule = module {
    viewModelOf(::FileListViewModel)
    viewModelOf(::FilePreviewViewModel)
}
