package maia.dmt.fileshare.presentation.fileList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.textFields.DmtSearchTextField
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.fileshare.presentation.components.FileDocumentCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FileListRoot(
    viewModel: FileListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddDocument: () -> Unit,
    onNavigateToFilePreview: (fileId: String, fileName: String, fileType: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is FileListEvent.NavigateBack -> onNavigateBack()
            is FileListEvent.NavigateToAddDocument -> onNavigateToAddDocument()
            is FileListEvent.NavigateToFilePreview -> onNavigateToFilePreview(event.fileId, event.fileName, event.fileType)
        }
    }

    FileListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun FileListScreen(
    state: FileListState,
    onAction: (FileListAction) -> Unit
) {
    val searchTextState = rememberTextFieldState(initialText = state.searchQuery)

    LaunchedEffect(searchTextState.text) {
        onAction(FileListAction.OnSearchQueryChange(searchTextState.text.toString()))
    }

    DmtBaseScreen(
        titleText = "Documents",
        onIconClick = { onAction(FileListAction.OnBackClick) },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(12.dp))

                    DmtSearchTextField(
                        state = searchTextState,
                        modifier = Modifier.width(300.dp),
                        placeholder = "Search",
                        endIcon = Icons.Default.Search,
                        endIconContentDescription = "Search Icon",
                        onEndIconClick = {}
                    )

                    Spacer(modifier = Modifier.padding(12.dp))

                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else if (state.documents.isEmpty() && state.searchQuery.isNotBlank()) {
                        Text(
                            text = "No documents found matching \"${state.searchQuery}\"",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else if (state.documents.isEmpty()) {
                        Text(
                            text = "No documents found",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                count = state.documents.size,
                                key = { index -> state.documents[index].id }
                            ) { index ->
                                val document = state.documents[index]
                                FileDocumentCard(
                                    fileName = document.fileName,
                                    uploadDate = document.uploadedAt,
                                    uploaderName = document.uploadedByName,
                                    onClick = {
                                        onAction(
                                            FileListAction.OnDocumentClick(
                                                fileId = document.id,
                                                fileName = document.fileName,
                                                fileType = document.fileType
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { onAction(FileListAction.OnAddDocumentClick) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Document"
                    )
                }
            }
        }
    )
}
