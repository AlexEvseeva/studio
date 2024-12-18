package ua.rikutou.studio.ui.transport.list

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.transport.list.components.TransportFilterElement

@Composable
fun TransportListScreen(
    viewModel: TransportListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filer by viewModel.filter.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is TransportList.Event.OnNavigate -> {
                    navController.navigate(it.destionation)
                }
            }
        }
    }
    TransportListScreenContent(
        state = state,
        filter = filer,
        onAction = viewModel::onAction
    )

}

@Composable
private fun TransportListScreenContent(
    state: TransportList.State,
    filter: TransportFilter,
    onAction: (TransportList.Action) -> Unit
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
                title = stringResource(R.string.transportTitle),
                isSearchEnabled = state.isSearchEnabled,
                isSearchActive = state.isSearchActive,
                onSearch = {
                    onAction(TransportList.Action.OnSearch)
                },
                onCancel = {
                    onAction(TransportList.Action.OnCancel)
                },
                onSearchChanged = {
                    onAction(TransportList.Action.OnSearchChanged(it))
                },
                onAdd = {
                    onAction(
                        TransportList.Action.OnNavigate(
                            destionation = Screen.Transport.Edit(
                                transportId = null
                            )
                        )
                    )
                },
                onClearFilters = {
                    onAction(TransportList.Action.OnClearFilters)
                }
            )
        },
        backLayerContent = {
            TransportFilterElement(
                filter = filter,
                onTypeSelect = {
                    onAction(TransportList.Action.OnTypeSelect(type = it))
                },
                onDateFrom = {
                    onAction(TransportList.Action.OnFieldSelect(dateFrom = it))
                },
                onDateTo = {
                    onAction(TransportList.Action.OnFieldSelect(dateTo = it))
                },
                onSeatsFrom = {
                    onAction(TransportList.Action.OnFieldSelect(seatsFrom = it))
                },
                onSeatsTo = {
                    onAction(TransportList.Action.OnFieldSelect(seatsTo = it))
                },
                onOrderChange = {
                    onAction(TransportList.Action.OnOrderChange(order = it))
                }
            )
        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                ,
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
                        items = state.transport,
                        key = { it.transport.transportId }
                    ) { item ->
                        Item(
                            modifier = Modifier.fillMaxWidth(),
                            title = item.transport.mark,
                            checked = item.isSelected,
                            comment = item.transport.type.name,
                            onItemClick = {
                                onAction(
                                    TransportList.Action.OnNavigate(
                                        Screen.Transport.Details(transportId = item.transport.transportId)
                                    )
                                )
                            },
                            onAddToCart = {
                                onAction(
                                    TransportList.Action.OnAddToCart(
                                        transportId = item.transport.transportId,
                                    )
                                )
                            }

                        )
                    }
                }
            }
        }
    )

}