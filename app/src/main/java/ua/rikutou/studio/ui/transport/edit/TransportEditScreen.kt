package ua.rikutou.studio.ui.transport.edit

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.config.floatFieldMaxLength
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.ui.components.DatePickerModal
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.section.edit.SectionEdit
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

private const val TAG = "TransportEditScreen"

@Composable
fun TransportEditScreen(
    viewModel: TransportEditViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is TransportEdit.Event.OnBack -> navController.popBackStack()
                is TransportEdit.Event.OmMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    TransportEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransportEditScreenContent(
    state: TransportEdit.State,
    onAction: (TransportEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {

        var expanded by remember { mutableStateOf(false) }
        var typeExpanded by remember { mutableStateOf(false) }

        var itemPosition by remember {
            mutableStateOf(
                state.departments
                    ?.mapIndexed { index, department ->
                        if(department.departmentId == state.transport?.departmentId) {
                            index
                        } else null
                    }
                    ?.filterNotNull()
                    ?.first() ?: 0
            )
        }

        var typePositon by remember {
            mutableStateOf(
                state.transport?.type?.ordinal ?: 0
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(R.string.typeLabel)}: "
            )
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(vertical = 16.dp),
                expanded = typeExpanded,
                onExpandedChange = {
                    typeExpanded = !typeExpanded
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.transport?.type?.name ?: "",
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        Icons.Default.ArrowDropDown, null
                    )
                }

                DropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = {
                        typeExpanded = false
                    }
                ) {
                    TransportType.entries.forEachIndexed() { index, type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = type.name
                                )
                            },
                            onClick = {
                                typeExpanded = false
                                typePositon = index
                                onAction(TransportEdit.Action.OnTypeSelect(type = type))
                            }
                        )
                    }

                }

            }

        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.transport?.mark ?: "",
            onValueChange = {
                onAction(TransportEdit.Action.OnFieldChanged(mark = it))
            },
            label = {
                Text(text = stringResource(R.string.mark))
            }
        )

        Text(
            modifier = Modifier
                .clickable {
                    onAction(TransportEdit.Action.OnSelectDate)
                }
            ,
            text = (state.transport?.manufactureDate ?: Date()).toString()
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.transport?.seats?.let {
                it.toString()
            } ?: "",
            onValueChange = {
                it.runCatching { toInt() }.getOrNull()?.let {
                    onAction(TransportEdit.Action.OnFieldChanged(seats = it))
                }
            },
            label = {
                Text(text = stringResource(R.string.seatsLabel))
            }
        )

        if(state.departments?.isNotEmpty() == true) {
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
                            text = state.departments[itemPosition].type,
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
                        state.departments.forEachIndexed { index, dept ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = dept.type
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    itemPosition = index
                                    onAction(TransportEdit.Action.OnDepartmentSelect(departmentId = dept.departmentId))
                                }
                            )
                        }

                    }

                }

            }

        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.transport?.color ?: "",
            onValueChange = {
                onAction(TransportEdit.Action.OnFieldChanged(color = it))
            },
            label = {
                Text(text = stringResource(R.string.colorLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.transport?.technicalState ?: "",
            onValueChange = {
                onAction(TransportEdit.Action.OnFieldChanged(technicalState = it))
            },
            label = {
                Text(text = stringResource(R.string.technicalStateLabel))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = state.transport?.rentPrice?.let { it.toString() } ?: "",
            onValueChange = {
                if(it.length < floatFieldMaxLength) {
                    onAction(TransportEdit.Action.OnFieldChanged(rentPrice = it))
                }
            },
            label = {
                Text(
                    text = stringResource(R.string.rentPriceLabel)
                )
            }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onAction(TransportEdit.Action.OnSave)
            }
        ) {
            Text(
                text = stringResource(R.string.save),
                color = Color.White
            )
        }

        if(state.isSelectDateDialogActive) {
            DatePickerModal(
                onDateSelected = { time ->
                    Log.d(TAG, "TransportEditScreenContent: $time")
                    time?.let {
                        onAction(TransportEdit.Action.OnFieldChanged(manufactureDate = Date(it)))
                    }
                },
                onDismiss = {
                    onAction(TransportEdit.Action.OnDismissDatePicker)
                },
                selectableDates = TransportSelectableDates
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object TransportSelectableDates : SelectableDates {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        LocalDate.ofInstant(Instant.ofEpochMilli(utcTimeMillis), ZoneId.systemDefault()) <= LocalDate.now()
}
