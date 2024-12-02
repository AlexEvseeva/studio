package ua.rikutou.studio.ui.transport.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.DeleteDialog
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle

@Composable
fun TransportDetailsScreen(
    viewModel: TransportDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is TransportDetails.Event.OnNavigate -> {
                    if (it.destination == null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it.destination)
                    }
                }
            }
        }
    }
    TransportDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransportDetailsScreenContent(
    state: TransportDetails.State,
    onAction: (TransportDetails.Action) -> Unit
) {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            DeleteDialog(
                modifier = Modifier.fillMaxWidth(),
                onOk = {
                    onAction(TransportDetails.Action.OnDelete)
                    coroutineScope.launch {
                        bottomSheetState.bottomSheetState.hide()
                    }
                },
                onCancel = {
                    coroutineScope.launch {
                        bottomSheetState.bottomSheetState.hide()
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
            ) {
                ElementTitle(
                    modifier = Modifier,
                    title = state.transport?.mark ?: "",
                    isEditEnabled = true,
                    onEdit = {
                        onAction(
                            TransportDetails.Action.OnNavigate(
                                destination = Screen.Transport.Edit(
                                    transportId = state.transport?.transportId
                                )
                            )
                        )
                    }
                )

                ElementContent(label = stringResource(R.string.typeLabel), name = state.transport?.type ?: "")
                ElementContent(label = stringResource(R.string.mark), name = state.transport?.mark ?: "")
                ElementContent(label = stringResource(R.string.manufactoreDate), name = state.transport?.manufactureDate.toString())
                ElementContent(label = stringResource(R.string.seatsLabel), name = (state.transport?.seats ?: 0).toString())
                ElementContent(label = stringResource(R.string.colorLabel), name = state.transport?.color ?: "")
                ElementContent(label = stringResource(R.string.technicalStateLabel), name = state.transport?.technicalState ?: "")

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            }
        },
    )

}