package ua.rikutou.studio.ui.location.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.ui.components.ElementTitle
import androidx.compose.foundation.lazy.items
import ua.rikutou.studio.ui.components.ImageItem

@Composable
fun LocationDetailsScreen(
    viewModel: LocationDetailsViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is LocationDetails.Event.OnNavigate -> navController.navigate(it.destination)
            }
        }
    }
    LocationDetailsScreenContent(state = state, onAction = viewModel::onAction)
}

@Composable
fun LocationDetailsScreenContent(
    state: State<LocationDetails.State>,
    onAction: (LocationDetails.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElementTitle(
            modifier = Modifier,
            title = state.value.location?.location?.name ?: ""
        )
        if(state.value.location?.images?.isNotEmpty() == true) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    items = state.value.location!!.images,
                    key = {it.galleryId}
                ) { item ->
                    ImageItem(
                        modifier = Modifier,
                        imageURL = item.url
                    )
                }
            }
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun LocationDetailsScreenContentPreview(modifier: Modifier = Modifier) {
    LocationDetailsScreenContent(state = remember { mutableStateOf(LocationDetails.State()) }) { }

}