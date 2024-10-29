package ua.rikutou.studio.ui.location.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item

@Composable
fun LocationListScreen(
    viewModel: LocationListViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle(LocationList.State())

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
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
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        ElementTitle(
            modifier = Modifier,
            title = "Locations"
        )
        LazyColumn(
            modifier = Modifier.weight(1F),
        ) {
            items(
                items = state.value.locations,
                key = { it.locationId }
            ) { item ->
                Item(
                    modifier = Modifier.fillMaxWidth(),
                    imageURL = "https://www.google.com/url?https://dynamic-media-cdn.tripadvisor.com/media/photo-o/10/c4/23/16/highland-view-bed-and.jpg?w=1200&h=-1&s=1",
                    title = item.name,
                    comment = item.address
                    )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun LocationListScreenContentPreview(modifier: Modifier = Modifier) {
    LocationListScreenContent(state = remember { mutableStateOf(LocationList.State()) }) { }
}