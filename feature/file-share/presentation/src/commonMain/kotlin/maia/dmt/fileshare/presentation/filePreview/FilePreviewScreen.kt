package maia.dmt.fileshare.presentation.filePreview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FilePreviewRoot(
    fileId: String,
    fileName: String,
    fileType: String,
    viewModel: FilePreviewViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(fileId) {
        viewModel.load(fileId, fileName, fileType)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is FilePreviewEvent.NavigateBack -> onNavigateBack()
            is FilePreviewEvent.OpenExternalUrl -> uriHandler.openUri(event.url)
        }
    }

    FilePreviewScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun FilePreviewScreen(
    state: FilePreviewState,
    onAction: (FilePreviewAction) -> Unit
) {
    DmtBaseScreen(
        titleText = state.fileName,
        onIconClick = { onAction(FilePreviewAction.OnBackClick) },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }
                    state.error != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Failed to load file",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = state.error.asString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    state.fileUrl != null -> {
                        SubcomposeAsyncImage(
                            model = state.fileUrl,
                            contentDescription = state.fileName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    CircularProgressIndicator()
                                }
                                is AsyncImagePainter.State.Error -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Failed to load image",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                else -> {
                                    SubcomposeAsyncImageContent()
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
