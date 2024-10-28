package ua.rikutou.studio.ui.studio.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
        ElementTitle(
            title = state.value.name?.let {
                "Edit ${state.value.name}"
            } ?: "Create new studio",
            isEditEnabled = false
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.name ?: "",
            onValueChange = {
                onAction(StudioEdit.Action.OnFieldchanged(name = it))
            },
            label = {
                Text(text = "name")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.address ?: "",
            onValueChange = {
                onAction(StudioEdit.Action.OnFieldchanged(address = it))
            },
            label = {
                Text(text = "address")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.postIndex ?: "",
            onValueChange = {
                onAction(StudioEdit.Action.OnFieldchanged(postIndex = it))
            },
            label = {
                Text(text = "postIndex")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.site ?: "",
            onValueChange = {
                onAction(StudioEdit.Action.OnFieldchanged(site = it))
            },
            label = {
                Text(text = "site")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.youtube ?: "",
            onValueChange = {
                onAction(StudioEdit.Action.OnFieldchanged(youtube = it))
            },
            label = {
                Text(text = "youtube")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.facebook ?: "",
            onValueChange = {
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
    StudioEditScreenContent(state = remember { mutableStateOf(StudioEdit.State(
        studioId = 1L,
        name = "sdfsdf",
        address = "jdfjklsdf",
        postIndex = "2345",
        site = "ksdfsdf",
        youtube = "sdfshj",
        facebook = "l;kfgh"
    )) }) { }
}