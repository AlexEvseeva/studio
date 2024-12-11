package ua.rikutou.studio.ui.equipment.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.rikutou.studio.R
import ua.rikutou.studio.data.remote.equipment.EquipmentType
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.ui.equipment.list.EquipmentFilter
import ua.rikutou.studio.ui.equipment.list.EquipmentOrder
import ua.rikutou.studio.ui.location.list.LocationOrder

@Composable
fun EquipmentFilterElement(
    modifier: Modifier = Modifier,
    filter: EquipmentFilter,
    onType: (EquipmentType?) -> Unit,
    onOrder: (EquipmentOrder) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .verticalScroll(state= rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .weight(1F)
            ) {
                Text(
                    text = stringResource(R.string.filterByEquipmentType)
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = filter.byType == null,
                        onClick = {
                            onType(null)
                        }
                    )
                    Text(
                        text = stringResource(R.string.all)
                    )
                }
                EquipmentType.entries.forEach { type ->
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = filter.byType == type,
                            onClick = {
                                onType(type)
                            }
                        )
                        Text(
                            text = type.name
                        )
                    }

                }

            }
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .weight(1F)
            ) {
                Text(
                    text = stringResource(R.string.orderByPrice)
                )
                EquipmentOrder.entries.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = filter.order == it,
                            onClick = {
                                onOrder(it)
                            }
                        )
                        Text(
                            text = it.name
                        )
                    }
                }
            }
        }

    }

}