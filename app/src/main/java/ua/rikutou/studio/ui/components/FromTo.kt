package ua.rikutou.studio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.rikutou.studio.R

@Composable
fun FromTo(
    modifier: Modifier = Modifier,
    title: String,
    from: Int?,
    to: Int?,
    onFromChange: (Int) -> Unit,
    onToChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1F),
            value = from?.let{ from.toString() } ?: "",
            onValueChange = {
                it.filter { it.toString().isNotEmpty() }.runCatching { it.toInt() }.getOrNull()?.let(onFromChange)
            },
            label = {
                Text(text = stringResource(R.string.from))
            }
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp)
            ,
            text = title,
        )
        OutlinedTextField(
            modifier = Modifier
                .weight(1F),
            value = to?.let{ to.toString() } ?: "",
            onValueChange = {
                it.filter { it.toString().isNotEmpty() }.runCatching { it.toInt() }.getOrNull()?.let(onToChange)
            },
            label = {
                Text(text = stringResource(R.string.to))
            }
        )

    }

}