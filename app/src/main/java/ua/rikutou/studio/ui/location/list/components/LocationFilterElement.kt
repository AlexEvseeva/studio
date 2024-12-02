package ua.rikutou.studio.ui.location.list.components

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.rikutou.studio.R
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.ui.components.FromTo
import ua.rikutou.studio.ui.location.edit.LocationEdit
import ua.rikutou.studio.ui.location.list.Dimensions
import ua.rikutou.studio.ui.location.list.LocationFilter
import ua.rikutou.studio.ui.location.list.LocationList

@Composable
fun LocationFilterElement(
    modifier: Modifier = Modifier,
    state: LocationList.State,
    filter: LocationFilter,
    onTypeSelect: (LocationType?) -> Unit,
    onDimentionsChange: (Dimensions) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .verticalScroll(state= rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .selectableGroup()
        ) {
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
            LocationType.entries.forEach { type ->
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
        FromTo(
            title = stringResource(R.string.width),
            from = filter.dimensions?.widthFrom,
            to = filter.dimensions?.widthTo,
            onFromChange = {
                onDimentionsChange(
                    filter.dimensions?.copy(widthFrom = it) ?: Dimensions(widthFrom = it)
                )
            },
            onToChange = {
                onDimentionsChange(
                    filter.dimensions?.copy(widthTo = it) ?: Dimensions(widthTo = it)
                )
            }
        )
        FromTo(
            title = stringResource(R.string.length),
            from = filter.dimensions?.lengthFrom,
            to = filter.dimensions?.lengthTo,
            onFromChange = {
                onDimentionsChange(
                    filter.dimensions?.copy(lengthFrom = it) ?: Dimensions(lengthFrom = it)
                )
            },
            onToChange = {
                onDimentionsChange(
                    filter.dimensions?.copy(lengthTo = it) ?: Dimensions(lengthTo = it)
                )
            }
        )
        FromTo(
            title = stringResource(R.string.height),
            from = filter.dimensions?.heightFrom,
            to = filter.dimensions?.heightTo,
            onFromChange = {
                onDimentionsChange(
                    filter.dimensions?.copy(heightFrom = it) ?: Dimensions(heightFrom = it)
                )
            },
            onToChange = {
                onDimentionsChange(
                    filter.dimensions?.copy(heightTo = it) ?: Dimensions(heightTo = it)
                )
            }
        )
        Spacer(modifier= Modifier.height(16.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun LocationDetailsPreview(modifier: Modifier = Modifier) {
    LocationFilterElement(
        state = LocationList.State(),
        filter = LocationFilter(),
        onTypeSelect = {},
        onDimentionsChange = {}
    )
}