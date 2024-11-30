package ua.rikutou.studio.ui.equipment.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.location.list.LocationList

@Composable
fun EquipmentListScreen(
    viewModel: EquipmentListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is EquipmentList.Event.OnNavigate -> {
                    navController.navigate(it.destionation)
                }
            }
        }
    }

    EquipmentListScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EquipmentListScreenContent(
    state: EquipmentList.State,
    onAction: (EquipmentList.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        ElementTitle(
            modifier = Modifier,
            title = stringResource(R.string.equipmentScreenTitle),
            isSearchEnabled = state.isSearchEnabled,
            isSearchActive = state.isSearchActive,
            onSearch = {
                onAction(EquipmentList.Action.OnSearch)
            },
            onCancel = {
                onAction(EquipmentList.Action.OnCancel)
            },
            onSearchChanged = {
                onAction(EquipmentList.Action.OnSearchChanged(it))
            },
            onAdd = {
                onAction(
                    EquipmentList.Action.OnNavigate(
                        destionation = Screen.Equipment.Edit(
                            equipmentId = null
                        )
                    )
                )
            }
        )
        LazyColumn(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = state.equipment,
                key = { it.equipmentId }
            ) { item ->
                Item(
                    modifier = Modifier.fillMaxWidth(),
                    title = item.name,
                    comment = item.comment
                ) {
                    onAction(
                        EquipmentList.Action
                            .OnNavigate(
                                Screen.Equipment.Details(
                                    equipmentId = item.equipmentId
                                )
                            )
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun EquipmentListScreenPreview(modifier: Modifier = Modifier) {
    EquipmentListScreenContent(
        state = EquipmentList.State()
    ) { }
}