package ua.rikutou.studio.ui.equipment.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.equipment.list.components.EquipmentFilterElement
import ua.rikutou.studio.ui.location.list.LocationList
import ua.rikutou.studio.ui.location.list.components.LocationFilterElement

@Composable
fun EquipmentListScreen(
    viewModel: EquipmentListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()

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
        filter = filter,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EquipmentListScreenContent(
    state: EquipmentList.State,
    filter: EquipmentFilter,
    onAction: (EquipmentList.Action) -> Unit
) {
    BackdropScaffold(
        modifier = Modifier
            .background(color = Color(0xFFF5F5F5))
        ,
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed),
        frontLayerShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        appBar = {
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
                },
                onClearFilters = {
                    onAction(EquipmentList.Action.OnClearFilter)
                }
            )
        },
        backLayerContent = {
            EquipmentFilterElement(
                filter = filter,
                onType = {
                    onAction(EquipmentList.Action.OnType(type = it))
                },
                onOrder = {
                    onAction(EquipmentList.Action.OnOrder(order = it))
                }
            )
        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(100.dp)
                        .height(6.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))

                )

                LazyColumn(
                    modifier = Modifier.weight(1F),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = state.equipment,
                        key = { it.equipment.equipmentId }
                    ) { item ->
                        Item(
                            modifier = Modifier.fillMaxWidth(),
                            title = item.equipment.name,
                            comment = item.equipment.comment,
                            checked = item.isSelected,
                            onItemClick = {
                                onAction(
                                    EquipmentList.Action
                                        .OnNavigate(
                                            Screen.Equipment.Details(
                                                equipmentId = item.equipment.equipmentId
                                            )
                                        )
                                )
                            },
                            onAddToCart = {
                                onAction(EquipmentList.Action.OnAddToCart(equipmentId = item.equipment.equipmentId))
                            }
                        )
                    }
                }
            }
        }
    )


}

@Preview(showSystemUi = true)
@Composable
private fun EquipmentListScreenPreview(modifier: Modifier = Modifier) {
    EquipmentListScreenContent(
        state = EquipmentList.State(),
        filter = EquipmentFilter()
    ) { }
}