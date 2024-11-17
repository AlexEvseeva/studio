package ua.rikutou.studio.ui.location.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ua.rikutou.studio.R
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.studio.edit.StudioEdit

@Composable
fun LocationEditScreen(
    viewModel: LocationEditViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is LocationEdit.Event.OnBack -> navController.popBackStack()
            }
        }
    }
    LocationEditScreenContent(state = state, onAction = viewModel::onAction)

}

@Composable
fun LocationEditScreenContent(
    state: State<LocationEdit.State>,
    onAction: (LocationEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {
        ElementTitle(
            title = state.value.location?.name ?: "",
            isEditActive = true
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.name ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(name = it))
            },
            label = {
                Text(text = "Name")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.address ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(address = it))
            },
            label = {
                Text(text = "Address")
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.width?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(width = it.toFloat()))
            },
            label = {
                Text(text = "Width")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.length?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(length = it.toFloat()))
            },
            label = {
                Text(text = "Length")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.height?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(height = it.toFloat()))
            },
            label = {
                Text(text = "Height")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.type ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(type = it))
            },
            label = {
                Text(text = "Type")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.rentPrice?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                onAction(LocationEdit.Action.OnFieldChanged(rentPrice = it.toFloat()))
            },
            label = {
                Text(text = "Rent price")
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onAction(LocationEdit.Action.OnSave)
            }
        ) {
            Text(stringResource(R.string.save))
        }
    }
}