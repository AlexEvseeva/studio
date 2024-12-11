package ua.rikutou.studio.ui.transport.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.rikutou.studio.R
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.ui.components.FromTo
import ua.rikutou.studio.ui.transport.list.TransportFilter
import ua.rikutou.studio.ui.transport.list.TransportOrder
import java.util.Date

@Composable
fun TransportFilterElement(
    modifier: Modifier = Modifier,
    filter: TransportFilter,
    onTypeSelect: (TransportType?) -> Unit,
    onDateFrom: (Date?) -> Unit,
    onDateTo: (Date?) -> Unit,
    onSeatsFrom: (Int) -> Unit,
    onSeatsTo: (Int) -> Unit,
    onOrderChange: (TransportOrder) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .weight(1F)
            ) {
                Text(
                    text = stringResource(R.string.filterByTransportType)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = filter.byType == null,
                        onClick = {
                            onTypeSelect(null)
                        }
                    )
                    Text(
                        text = stringResource(R.string.all)
                    )
                }
                TransportType.entries.forEach { type ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = filter.byType?.name == type.name,
                            onClick = {
                                onTypeSelect(type)
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
                    text = stringResource(R.string.orderByDate)
                )
                TransportOrder.entries.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = filter.order.name == it.name,
                            onClick = {
                                onOrderChange(it)
                            }
                        )
                        Text(
                            text = it.name
                        )
                    }
                }
            }
        }


        FromTo(
            title = stringResource(R.string.manufactoreDate),
            from = filter.manufactureDateFrom,
            to = filter.manufactureDateTo,
            onFromChange = {
                onDateFrom(it)
            },
            onToChange = {
                onDateTo(it)
            }
        )
        FromTo(
            title = stringResource(R.string.seatsLabel),
            from = filter.seatsFrom,
            to = filter.seatsTo,
            onFromChange = {
                onSeatsFrom(it)
            },
            onToChange = {
                onSeatsTo(it)
            }
        )

        Spacer(modifier= Modifier.height(16.dp))
    }

}