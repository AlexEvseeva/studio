package ua.rikutou.studio.ui.dept.edit

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.location.edit.LocationEdit

@Composable
fun DepartmentEditScreen(
    viewModel: DepartmentEditViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is DepartmentEdit.Event.OnBack -> navController.popBackStack()
            }
        }
    }

    DepartmentEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun DepartmentEditScreenContent(
    state: DepartmentEdit.State,
    onAction: (DepartmentEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {
        ElementTitle(
            title = state.department?.entity?.type ?: "",
            isEditActive = true,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.department?.entity?.type ?: "",
            onValueChange = {
                onAction(DepartmentEdit.Action.OnFieldChanged(type = it))
            },
            label = {
                Text(text = stringResource(R.string.typeLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.department?.entity?.workHours ?: "",
            onValueChange = {
                onAction(DepartmentEdit.Action.OnFieldChanged(workHours = it))
            },
            label = {
                Text(text = stringResource(R.string.workHoursLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.department?.entity?.contactPerson ?: "",
            onValueChange = {
                onAction(DepartmentEdit.Action.OnFieldChanged(contactPerson = it))
            },
            label = {
                Text(text = stringResource(R.string.contectPersonLabel))
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onAction(DepartmentEdit.Action.OnSave)
            }
        ) {
            Text(
                text = stringResource(R.string.save),
                color = Color.White
            )
        }
    }

}

@Preview
@Composable
fun DepartmentEditScreenContentPreview(modifier: Modifier = Modifier) {
    DepartmentEditScreenContent(
        state = DepartmentEdit.State(),
        onAction = {}
    )
}