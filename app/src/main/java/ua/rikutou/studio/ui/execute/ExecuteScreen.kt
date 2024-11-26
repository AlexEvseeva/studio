package ua.rikutou.studio.ui.execute

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R

@Composable
fun ExecuteScreen(
    viewModel: ExecuteViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is Execute.Event.OnMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ExecuteScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ExecuteScreenContent(
    state: Execute.State,
    onAction: (Execute.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 4.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ,
            value = state.query,
            onValueChange = {
                onAction(Execute.Action.OnQueryStringChanged(query = it))
            },
            label = {
                Text(
                    text = stringResource(R.string.sqlQueryLabel)
                )
            },
            shape = RoundedCornerShape(16.dp),
            minLines = 4,
            maxLines = 4,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
            ,
            onClick = {
                onAction(Execute.Action.OnClear)
            }
        ) {
            Text(
                text = stringResource(R.string.clear),
                color = Color.White
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
            ,
            onClick = {
                onAction(Execute.Action.OnQuery)
            }
        ) {
            Text(
                text = stringResource(R.string.query),
                color = Color.White
            )
        }
        if(state.columnNames.size > 0) {
            LazyVerticalGrid(
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(1F)
                ,
                columns = GridCells.Fixed(state.columnNames.size)
            ) {
                items(state.columnNames) { item ->
                    Text(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(2.dp))
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(2.dp))
                            .background(color = Color.LightGray)
                            .padding(horizontal = 2.dp)
                        ,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = item,
                        fontSize = 8.sp
                    )
                }
                state.queryResult.forEach { list ->
                    items(list) { item ->
                        Text(
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(2.dp))
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp)
                            ,
                            text = item,
                            fontSize = 8.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }

    }

}

@Preview(showSystemUi = true)
@Composable
private fun ExecuteScreenContentPreview(modifier: Modifier = Modifier) {
    ExecuteScreenContent(
        state = Execute.State(),
        onAction = {}
    )
}