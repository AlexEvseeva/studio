package ua.rikutou.studio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.rikutou.studio.R

@Composable
fun ElementTitle(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color = Color.Black,
    isEditEnabled: Boolean = false,
    isEditActive: Boolean = false,
    isSearchEnabled: Boolean = false,
    isSearchActive: Boolean = false,
    onEdit: () -> Unit = {},
    onAdd: () -> Unit = {},
    onSearchChanged: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onCancel: () -> Unit = {},
    onClearFilters: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        var search by remember {
            mutableStateOf("")
        }
        when {
            isEditEnabled -> {
                Text(
                    modifier = modifier
                        .weight(1f),
                    text =title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    color = textColor
                )
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onEdit()
                        },
                    painter = painterResource(R.drawable.edit),
                    contentDescription = "Edit",
                )
            }
            isSearchEnabled -> {
                Text(
                    modifier = modifier
                        .weight(1f),
                    text =title,
                    color = textColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onClearFilters()
                        },
                    painter = painterResource(R.drawable.clear),
                    contentDescription = "Clear"
                )
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onSearch()
                        },
                    painter = painterResource(R.drawable.search),
                    contentDescription = "Search"
                )
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onAdd()
                        },
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Add"
                )
            }
            isSearchActive -> {
                OutlinedTextField(
                    modifier = modifier
                        .weight(1f),
                    value = search,
                    onValueChange = {
                        search = it
                        onSearchChanged(it)
                    }
                )
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onCancel()
                        },
                    painter = painterResource(R.drawable.clear),
                    contentDescription = "Clear"
                )
            }
            isEditActive -> {
                Text(
                    modifier = modifier
                        .weight(1f),
                    text =title,
                    color = textColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            else -> {
                Text(
                    modifier = modifier
                        .weight(1f),
                    text = title,
                    color = textColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ElementTitlePreview(modifier: Modifier = Modifier) {
    Column {
        ElementTitle(title = "Black crane", isEditEnabled = true)
        ElementTitle(title = "Black crane", isSearchActive = true)
        ElementTitle(title = "Black crane", isSearchEnabled = true)
        ElementTitle(title = "Black crane", isEditActive = true)

    }
}