package ua.rikutou.studio.ui.location.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementContent
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
                is LocationDetails.Event.OnNavigate -> {
                    if (it.destination == null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it.destination)
                    }
                }
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
            .padding(horizontal = 4.dp)
    ) {
        ElementTitle(
            modifier = Modifier,
            title = state.value.location?.location?.name ?: "",
            isEditEnabled = true,
            onEdit = {
                onAction(
                    LocationDetails.Action.OnNavigate(
                        destination = Screen.Location.Edit(
                            locationId = state.value.location?.location?.locationId
                        )
                    )
                )
            }
        )
        if(state.value.location?.images?.isNotEmpty() == true) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
        ElementContent(label = "Name", name = state.value.location?.location?.name ?: "")
        ElementContent(label = "Address", name = state.value.location?.location?.address ?: "")
        ElementContent(label = "Area", name = "${state.value.location?.location?.width ?: 0} x ${state.value.location?.location?.length ?: 0} x ${state.value.location?.location?.height ?: 0}")
        ElementContent(label = "Type", name = state.value.location?.location?.type ?: "")
        ElementContent(label = "Rent price", name = "$${state.value.location?.location?.rentPrice ?: 0F}")

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onAction(LocationDetails.Action.OnDelete)
            }
        ) {
            Text(text = stringResource(R.string.delete))
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun LocationDetailsScreenContentPreview(modifier: Modifier = Modifier) {
    LocationDetailsScreenContent(state = remember { mutableStateOf(LocationDetails.State()) }) { }

}