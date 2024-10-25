package ua.rikutou.studio.ui.studio.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.ui.components.ElementTitle

@Composable
fun StudioEditScreen(
    viewModel: StudioEditViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle(StudioEdit.State())

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {

        }
    }
    StudioEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun StudioEditScreenContent(
    state: State<StudioEdit.State>,
    onAction: (StudioEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var name by remember { mutableStateOf(state.value.studio?.name ?: "") }
        var address by remember { mutableStateOf(state.value.studio?.address ?: "") }
        var postIndex by remember { mutableStateOf(state.value.studio?.postIndex ?: "") }
        var site by remember { mutableStateOf(state.value.studio?.site ?: "") }
        var youtube by remember { mutableStateOf(state.value.studio?.youtube ?: "") }
        var facebook by remember { mutableStateOf(state.value.studio?.facebook ?: "") }

        ElementTitle(
            title = state.value.studio?.let {
                "Edit ${state.value.studio?.name}"
            } ?: "Create new studio",
            isEditEnabled = false
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = name,
            onValueChange = {
                name = it
                onAction(StudioEdit.Action.OnFieldchanged(name = it))
            },
            label = {
                Text(text = "name")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = address,
            onValueChange = {
                address = it
                onAction(StudioEdit.Action.OnFieldchanged(address = it))
            },
            label = {
                Text(text = "address")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = postIndex,
            onValueChange = {
                postIndex = it
                onAction(StudioEdit.Action.OnFieldchanged(postIndex = it))
            },
            label = {
                Text(text = "postIndex")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = site,
            onValueChange = {
                site = it
                onAction(StudioEdit.Action.OnFieldchanged(site = it))
            },
            label = {
                Text(text = "site")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = youtube,
            onValueChange = {
                youtube = it
                onAction(StudioEdit.Action.OnFieldchanged(youtube = it))
            },
            label = {
                Text(text = "youtube")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = facebook,
            onValueChange = {
                facebook = it
                onAction(StudioEdit.Action.OnFieldchanged(facebook = it))
            },
            label = {
                Text(text = "facebook")
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = {}
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun StudioEditScreenContentPreview() {
    StudioEditScreenContent(state = remember { mutableStateOf(StudioEdit.State(studio = StudioEntity(
        studioId = 1L,
        name = "sdfsdf",
        address = "jdfjklsdf",
        postIndex = "2345",
        site = "ksdfsdf",
        youtube = "sdfshj",
        facebook = "l;kfgh"
    ))) }) { }
}