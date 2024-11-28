package ua.rikutou.studio.ui.section.edit

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.ui.components.ElementTitle

private const val TAG ="SectionEditScreen"

@Composable
fun SectionEditScreen(
    viewModel: SectionEditViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is SectionEdit.Event.OnBack -> navController.popBackStack()
            }
        }
    }

    SectionEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SectionEditScreenContent(
    state: SectionEdit.State,
    onAction: (SectionEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {

        var expanded by remember { mutableStateOf(false) }
        var itemPosition by remember {
            mutableStateOf(
                state.departments
                    ?.mapIndexed { index, department ->
                        if(department.departmentId == state.section?.departmentId) {
                            index
                        } else null
                    }
                    ?.filterNotNull()
                    ?.first() ?: 0
            )
        }


        ElementTitle(
            title = state.section?.title ?: "",
            isEditActive = true
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.section?.title ?: "",
            onValueChange = {
                onAction(SectionEdit.Action.OnFieldChanged(title = it))
            },
            label = {
                Text(text = stringResource(R.string.titleLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.section?.address ?: "",
            onValueChange = {
                onAction(SectionEdit.Action.OnFieldChanged(address = it))
            },
            label = {
                Text(text = stringResource(R.string.addressLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.section?.internalPhoneNumber ?: "",
            onValueChange = {
                onAction(SectionEdit.Action.OnFieldChanged(internalPhoneNumber = it))
            },
            label = {
                Text(text = stringResource(R.string.internalPhoneNumberLabel))
            }
        )


        state.departments?.let { departments ->
            Log.d(TAG, "SectionEditScreenContent: item position: $itemPosition")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.departmentLabel)}: "
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.padding(vertical = 16.dp),
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = departments[itemPosition].type,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            Icons.Default.ArrowDropDown, null
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        departments.forEachIndexed { index, dept ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = dept.type
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    itemPosition = index
                                    onAction(SectionEdit.Action.OnDepartmentSelect(departmentId = dept.departmentId))
                                }
                            )
                        }

                    }

                }

            }

        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onAction(SectionEdit.Action.OnSave)
            }
        ) {
            Text(
                text = stringResource(R.string.save),
                color = Color.White
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun SectionEditScreenPreview(modifier: Modifier = Modifier) {
    SectionEditScreenContent(
        state = SectionEdit.State()
    ) { }
}