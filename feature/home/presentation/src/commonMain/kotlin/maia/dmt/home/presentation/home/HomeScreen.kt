package maia.dmt.home.presentation.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import maia.dmt.core.designsystem.components.layouts.DmtSnackbarScaffold
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
//    viewModel: HomeViewModel = koinViewModel(),
//    onHomeSuccess: (String) -> Unit
) {
//    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    HomeScreen(
//        state = state,
//        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HomeScreen(
//    state: HomeState,
//    onAction: (HomeAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    DmtSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        Text(
            text = "Home"
        )
    }
}