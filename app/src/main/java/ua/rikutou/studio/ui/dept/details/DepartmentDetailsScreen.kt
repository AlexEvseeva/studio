package ua.rikutou.studio.ui.dept.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.DeleteDialog
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item

@Composable
fun DepartmentDetailsScreen(
    viewModel: DepartmentDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is DepartmentDetails.Event.OnNavigate -> {
                    if (it.destination == null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it.destination)
                    }
                }
            }
        }
    }

    DepartmentDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentDetailsScreenContent(
    state: DepartmentDetails.State,
    onAction: (DepartmentDetails.Action) -> Unit
) {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            DeleteDialog(
                modifier = Modifier.fillMaxWidth(),
                onOk = {
                    onAction(DepartmentDetails.Action.OnDelete)
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
                    title = state.department?.entity?.type ?: "",
                    isEditEnabled = true,
                    onEdit = {
                        onAction(
                            DepartmentDetails.Action.OnNavigate(
                                destination = Screen.Department.Edit(
                                    departmentId = state.department?.entity?.departmentId
                                )
                            )
                        )
                    }
                )

                ElementContent(
                    label = stringResource(R.string.typeLabel),
                    name = state.department?.entity?.type ?: ""
                )
                ElementContent(
                    label = stringResource(R.string.workHoursLabel),
                    name = state.department?.entity?.workHours ?: ""
                )
                ElementContent(
                    label = stringResource(R.string.contectPersonLabel),
                    name = state.department?.entity?.contactPerson ?: ""
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .weight(1F)
                    ,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (state.department?.sections?.isNotEmpty() == true) {
                        item {
                            Text(
                                text = stringResource(R.string.sections),
                            )
                        }
                        items(state.department.sections) { section ->
                            Item(
                                title = section.title,
                                comment = section.address,
                                onItemClick = {
                                    onAction(
                                        DepartmentDetails.Action.OnNavigate(
                                            destination = Screen.Section.Details(
                                                sectionId = section.sectionId
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }

                    if(state.department?.transport?.isNotEmpty() == true) {
                        item {
                            Text(
                                text = stringResource(R.string.transport)
                            )
                        }
                        items(state.department.transport) { transport ->
                            Item(
                                title = transport.mark,
                                comment = transport.type,
                                onItemClick = {
                                    onAction(
                                        DepartmentDetails.Action.OnNavigate(
                                            destination = Screen.Transport.Details(
                                                transportId = transport.transportId
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }

                }


                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onAction(
                            DepartmentDetails.Action.OnNavigate(
                                destination = Screen.Section.Edit()
                            )
                        )
                    }
                ) {
                    Text(text = stringResource(R.string.addSection))
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

@Preview(showSystemUi = true)
@Composable
fun DepartmentDetailsContentPreview(modifier: Modifier = Modifier) {
    DepartmentDetailsScreenContent(
        state = DepartmentDetails.State()
    ) { }
}