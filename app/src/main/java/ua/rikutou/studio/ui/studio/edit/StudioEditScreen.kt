package ua.rikutou.studio.ui.studio.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch
import ua.rikutou.studio.R
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.DeleteDialog
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.ImageItem
import ua.rikutou.studio.ui.location.details.LocationDetails

@Composable
fun StudioEditScreen(
    viewModel: StudioEditViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                StudioEdit.Event.OnBack -> navController.popBackStack()
                is StudioEdit.Event.OnNavigate -> {
                    navController.navigate(it.destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
    StudioEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioEditScreenContent(
    state: State<StudioEdit.State>,
    onAction: (StudioEdit.Action) -> Unit
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
                    onAction(StudioEdit.Action.OnDelete)
                    coroutineScope.launch {
                        bottomSheetState.bottomSheetState.hide()
                        onAction(StudioEdit.Action.OnDelete)
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
                    .verticalScroll(state = rememberScrollState())
                    .padding(horizontal = 8.dp)
            ) {
                ElementTitle(
                    title = state.value.name?.let {
                        "Edit ${state.value.name}"
                    } ?: "Create new studio",
                    isEditActive = true
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = state.value.name ?: "",
                    onValueChange = {
                        onAction(StudioEdit.Action.OnFieldchanged(name = it))
                    },
                    label = {
                        Text(text = "name")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = state.value.address ?: "",
                    onValueChange = {
                        onAction(StudioEdit.Action.OnFieldchanged(address = it))
                    },
                    label = {
                        Text(text = "address")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = state.value.postIndex ?: "",
                    onValueChange = {
                        onAction(StudioEdit.Action.OnFieldchanged(postIndex = it))
                    },
                    label = {
                        Text(text = "postIndex")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = state.value.site ?: "",
                    onValueChange = {
                        onAction(StudioEdit.Action.OnFieldchanged(site = it))
                    },
                    label = {
                        Text(text = "site")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = state.value.youtube ?: "",
                    onValueChange = {
                        onAction(StudioEdit.Action.OnFieldchanged(youtube = it))
                    },
                    label = {
                        Text(text = "youtube")
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    value = state.value.facebook ?: "",
                    onValueChange = {
                        onAction(StudioEdit.Action.OnFieldchanged(facebook = it))
                    },
                    label = {
                        Text(text = "facebook")
                    }
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onClick = {
                        onAction(StudioEdit.Action.OnSave)
                    }
                ) {
                    Text(text = "Save")
                }
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

@Composable
@Preview(showSystemUi = true)
fun StudioEditScreenContentPreview() {
    StudioEditScreenContent(state = remember { mutableStateOf(StudioEdit.State(
        studioId = 1L,
        name = "sdfsdf",
        address = "jdfjklsdf",
        postIndex = "2345",
        site = "ksdfsdf",
        youtube = "sdfshj",
        facebook = "l;kfgh"
    )) }) { }
}