package ua.rikutou.studio.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.rikutou.studio.R
import java.text.SimpleDateFormat
import java.util.Date

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FromTo(
    modifier: Modifier = Modifier,
    title: String,
    from: Date?,
    to: Date?,
    onFromChange: (Date?) -> Unit,
    onToChange: (Date?) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val formater = SimpleDateFormat("YYYY-MM-dd")
        var showFromDialog by remember { mutableStateOf(false) }
        var showToDialog by remember { mutableStateOf(false) }

        Text(
            modifier = Modifier
                .weight(1F)
                .clickable {
                    showFromDialog = true
                }
            ,
            text = from?.let { formater.format(from) } ?: "YYYY-MM-DD",
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp)
            ,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            text = title,
        )
        Text(
            modifier = Modifier
                .weight(1F)
                .clickable {
                    showToDialog = true
                }
            ,
            text = to?.let { formater.format(to) } ?: "YYYY-MM-DD",
            fontSize = 12.sp
        )

        if(showFromDialog) {
            DatePickerModal(
                onDateSelected = {
                    showFromDialog = false
                    it?.let {
                        onFromChange(Date(it))
                    }
                },
                onDismiss = {
                    showFromDialog = false
                }
            )
        }
        if(showToDialog) {
            DatePickerModal(
                onDateSelected = {
                    showToDialog = false
                    it?.let {
                        onToChange(Date(it))
                    }
                },
                onDismiss = {
                    showToDialog = false
                }
            )
        }
    }
}