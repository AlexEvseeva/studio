package ua.rikutou.studio.ui.equipment.details

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
import ua.rikutou.studio.ui.components.ImageItem
import ua.rikutou.studio.ui.location.details.LocationDetails

@Composable
fun EquipmentDetailsScreen(
    viewModel: EquipmentDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is EquipmentDetails.Event.OnNavigate -> {
                    if (it.destination == null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it.destination)
                    }
                }
            }
        }
    }
    EquipmentDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EquipmentDetailsScreenContent(
    state: EquipmentDetails.State,
    onAction: (EquipmentDetails.Action) -> Unit
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
                    onAction(EquipmentDetails.Action.OnDelete)
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
                    title = state.equipment?.name ?: "",
                    isEditEnabled = true,
                    onEdit = {
                        onAction(
                            EquipmentDetails.Action.OnNavigate(
                                destination = Screen.Equipment.Edit(
                                    equipmentId = state.equipment?.equipmentId
                                )
                            )
                        )
                    }
                )

                ElementContent(label = stringResource(R.string.nameLabel), name = state.equipment?.name ?: "")
                ElementContent(label = stringResource(R.string.typeLabel), name = state.equipment?.type ?: "")
                ElementContent(label = stringResource(R.string.commentLabel), name = state.equipment?.comment ?: "")
                ElementContent(label = stringResource(R.string.rentPriceLabel), name = (state.equipment?.rentPrice ?: 0).toString())

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
private fun EquipmentScreenPreview(modifier: Modifier = Modifier) {
    EquipmentDetailsScreenContent(state = EquipmentDetails.State()) { }
}