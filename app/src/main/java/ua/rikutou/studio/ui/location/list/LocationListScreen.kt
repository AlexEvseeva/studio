package ua.rikutou.studio.ui.location.list

import android.util.Log
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
import ua.rikutou.studio.ui.location.list.components.LocationFilterElement

private val TAG by lazy { "LocationListScreen" }

@Composable
fun LocationListScreen(
    viewModel: LocationListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle(LocationList.State())
    val filer by viewModel.filter.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is LocationList.Event.OnNavigate -> {
                    navController.navigate(it.destionation)
                }
            }
        }
    }
    
    LocationListScreenContent(state = state, filter = filer, onAction = viewModel::onAction)

}

@Composable
private fun LocationListScreenContent(
    state: LocationList.State,
    filter: LocationFilter,
    onAction: (LocationList.Action) -> Unit
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
                title = stringResource(R.string.locationScreenTitle),
                isSearchEnabled = state.isSearchEnabled,
                isSearchActive = state.isSearchActive,
                onSearch = {
                    onAction(LocationList.Action.OnSearch)
                },
                onCancel = {
                    onAction(LocationList.Action.OnCancel)
                },
                onSearchChanged = {
                    onAction(LocationList.Action.OnSearchChanged(it))
                },
                onAdd = {
                    onAction(
                        LocationList.Action.OnNavigate(
                            destionation = Screen.Location.Edit(
                                locationId = null
                            )
                        )
                    )
                },
                onClearFilters = {
                    onAction(LocationList.Action.OnClearFilters)
                }
            )
        },
        backLayerContent = {
            LocationFilterElement(
                state = state,
                filter = filter,
                onTypeSelect = {
                    onAction(LocationList.Action.OnTypeSelect(it))
                },
                onDimentionsChange = {
                    onAction(LocationList.Action.OnDimansionsChange(dimensions = it))
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
                        items = state.locations,
                        key = { it.location.locationId }
                    ) { item ->
                        Item(
                            modifier = Modifier.fillMaxWidth(),
                            imageURL = if (item.images.isNotEmpty()) {
                                item.images.first().url
                            } else {
                                ""
                            },
                            title = item.location.name,
                            comment = item.location.address,
                            checked = item.location.locationId in state.selectedLocations,
                            onItemClick = {
                                onAction(LocationList.Action.OnNavigate(Screen.Location.Details(locationId = item.location.locationId)))
                            },
                            onCheckedChange = { checked ->
                                onAction(LocationList.Action.OnCheckedChange(locationId = item.location.locationId, checked = checked))
                            }
                        )
                    }
                }
            }
        }
    )

}

@Composable
@Preview(showSystemUi = true)
private fun LocationListScreenContentPreview(modifier: Modifier = Modifier) {
    LocationListScreenContent(state = LocationList.State(), filter = LocationFilter()) { }
}