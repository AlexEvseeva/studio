package ua.rikutou.studio.ui.location.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Text
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.BuildConfig
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item

private val TAG by lazy { "LocationListScreen" }

@Composable
fun LocationListScreen(
    viewModel: LocationListViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle(LocationList.State())

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is LocationList.Event.OnNavigate -> {
                    navController.navigate(it.destionation)
                }
            }
        }
    }
    LocationListScreenContent(state = state, onAction = viewModel::onAction)

}

@Composable
private fun LocationListScreenContent(
    state: State<LocationList.State>,
    onAction: (LocationList.Action) -> Unit
) {
    BackdropScaffold(
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed),
        frontLayerShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        appBar = {
            ElementTitle(
                modifier = Modifier,
                title = stringResource(R.string.locationScreenTitle),
                textColor = Color.White,
                isSearchEnabled = state.value.isSearchEnabled,
                isSearchActive = state.value.isSearchActive,
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
                }
            )
        },
        backLayerContent = {
            Text(
                modifier = Modifier.padding(vertical = 50.dp),
                text = "Filter layer here"
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
                        items = state.value.locations,
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
                            comment = item.location.address
                        ) {
                            onAction(LocationList.Action.OnNavigate(Screen.Location.Details(locationId = item.location.locationId)))
                        }
                    }
                }
            }
        }
    )

}

@Composable
@Preview(showSystemUi = true)
private fun LocationListScreenContentPreview(modifier: Modifier = Modifier) {
    LocationListScreenContent(state = remember { mutableStateOf(LocationList.State()) }) { }
}