package ua.rikutou.studio.ui.location.edit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ua.rikutou.studio.R
import ua.rikutou.studio.config.floatFieldMaxLength
import ua.rikutou.studio.config.locationAddressLength
import ua.rikutou.studio.config.locationNameLength
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.studio.edit.StudioEdit

private const val TAG = "LocationEditScreen"

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
                if(it.length < locationNameLength) {
                    onAction(LocationEdit.Action.OnFieldChanged(name = it))
                }
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
                if(it.length < locationAddressLength) {
                    onAction(LocationEdit.Action.OnFieldChanged(address = it))
                }
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
                if(it.length < floatFieldMaxLength) {
                    onAction(LocationEdit.Action.OnFieldChanged(width = it))
                }
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
                if(it.length < floatFieldMaxLength) {
                    onAction(LocationEdit.Action.OnFieldChanged(length = it))
                }
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
                if(it.length < floatFieldMaxLength) {
                    onAction(LocationEdit.Action.OnFieldChanged(height = it))
                }
            },
            label = {
                Text(text = "Height")
            }
        )

        Column(
            modifier = Modifier.selectableGroup()
        ) {
            LocationType.entries.map {
                it.name
            }.forEach { type ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start

                ) {
                    RadioButton(
                        selected = state.value.location?.type?.name == type,
                        onClick = {
                            onAction(LocationEdit.Action.OnTypeSelected(option = type))
                        }
                    )
                    Text(
                        text = type
                    )

                }

            }

        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.value.location?.rentPrice?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                if(it.length < floatFieldMaxLength) {
                    onAction(LocationEdit.Action.OnFieldChanged(rentPrice = it))
                }
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
            Text(
                text = stringResource(R.string.save),
                color = Color.White
            )
        }
    }
}