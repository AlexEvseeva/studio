package ua.rikutou.studio.ui.location.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

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
        Text("Locations")
    }
}

@Composable
@Preview(showSystemUi = true)
private fun LocationListScreenContentPreview(modifier: Modifier = Modifier) {
    LocationListScreenContent(state = remember { mutableStateOf(LocationList.State()) }) { }
}