package ua.rikutou.studio.ui.equipment.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.data.remote.equipment.EquipmentType
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.location.edit.LocationEdit

@Composable
fun EquipmentEditScreen(
    viewModel: EquipmentEditViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is EquipmentEdit.Event.OnBack -> navController.popBackStack()
            }
        }
    }
    EquipmentEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EquipmentEditScreenContent(
    state: EquipmentEdit.State,
    onAction: (EquipmentEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {
        ElementTitle(
            title = state.equipment?.name ?: "",
            isEditActive = true
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.equipment?.name ?: "",
            onValueChange = {
                onAction(EquipmentEdit.Action.OnFieldChanged(name = it))
            },
            label = {
                Text(text = stringResource(R.string.nameLabel))
            }
        )

        Column(
            modifier = Modifier.selectableGroup()
        ) {
            EquipmentType.entries.forEach { type ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start

                ) {
                    RadioButton(
                        selected = state.equipment?.type == type,
                        onClick = {
                            onAction(EquipmentEdit.Action.OnTypeSelected(type = type))
                        }
                    )
                    Text(
                        text = type.name
                    )

                }

            }

        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.equipment?.comment ?: "",
            onValueChange = {
                onAction(EquipmentEdit.Action.OnFieldChanged(comment = it))
            },
            label = {
                Text(text = stringResource(R.string.commentLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.equipment?.rentPrice?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                onAction(EquipmentEdit.Action.OnFieldChanged(rentPrice = it.toFloat()))
            },
            label = {
                Text(text = stringResource(R.string.rentPriceLabel))
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onAction(EquipmentEdit.Action.OnSave)
            }
        ) {
            Text(
                text = stringResource(R.string.save),
                color = Color.White
            )
        }
    }

}