package maia.dmt.home.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.home.presentation.generated.resources.Res
import dmtproms.feature.home.presentation.generated.resources.home_title
import dmtproms.feature.home.presentation.generated.resources.logout_icon
import dmtproms.feature.home.presentation.generated.resources.messages
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.components.layouts.DmtSnackbarScaffold
import maia.dmt.core.designsystem.components.layouts.DmtSurface
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.home.presentation.components.DmtMessageSection
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    HomeScreen(
        onAction = viewModel::onAction,
    )
}

@Composable
fun HomeScreen(
    onAction: (HomeAction) -> Unit,
) {

    DmtBaseScreen(
        titleText = stringResource(Res.string.home_title),
        iconBar = vectorResource(Res.drawable.logout_icon),
        onIconClick = { onAction(HomeAction.OnLogoutClick) },
        content = {
            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.padding(12.dp))
                DmtMessageSection(
                    title = stringResource(Res.string.messages),
                    messages = listOf(
                        "Don't forget take your test.",
                        "Take 2 pills at 12:00"
                    )
                )
            }

//            LazyHorizontalGrid(
//                rows = 2,
//                columns = 2,
//                modifier = Modifier.fillMaxSize(),
//            ) {
//
//            }
        }
    )
}

@Composable
@Preview
fun HomeScreenPrev() {
    DmtTheme {
        HomeScreen(
            onAction = {}
        )
    }
}