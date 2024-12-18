package ua.rikutou.studio.ui.film.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.DeleteDialog
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.components.yearFormatter

@Composable
fun FilmDetailsScreen(
    viewModel: FilmDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is FilmDetails.Event.OnNavigate -> {
                    if (it.destination == null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it.destination)
                    }
                }
            }
        }
    }
    FilmDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailsScreenContent(
    state: FilmDetails.State,
    onAction: (FilmDetails.Action) -> Unit
) {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            DeleteDialog(
                modifier = Modifier.fillMaxWidth(),
                onOk = {
                    onAction(FilmDetails.Action.OnDelete)
                    coroutineScope.launch {
                        bottomSheetState.bottomSheetState.hide()
                    }
                },
                onCancel = {
                    coroutineScope.launch {
                        bottomSheetState.bottomSheetState.hide()
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
            ) {
                ElementTitle(
                    modifier = Modifier,
                    title = state.film?.entity?.title ?: "",
                    isEditEnabled = false,
                    onEdit = {
                        onAction(
                            FilmDetails.Action.OnNavigate(
                                destination = Screen.Film.Edit(
                                    filmId = state.film?.entity?.filmId
                                )
                            )
                        )
                    }
                )
                ElementContent(label = stringResource(R.string.titleLabel), name = state.film?.entity?.title ?: "")
                ElementContent(label = stringResource(R.string.directorLabel), name = state.film?.entity?.director ?: "")
                ElementContent(label = stringResource(R.string.writerLabel), name = state.film?.entity?.writer ?: "")
                ElementContent(label = stringResource(R.string.yearLabel), name = state.film?.entity?.date?.let { yearFormatter.format(it) } ?: "")
                ElementContent(label = stringResource(R.string.budgetLabel), name = state.film?.entity?.budget?.let { it.toString() } ?: "")

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                    ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if(state.film?.genres?.isNotEmpty() == true) {
                        item {
                            Text(
                                text = stringResource(R.string.genreLabel)
                            )
                        }
                        items(state.film.genres) { genre ->
                            Text(
                                text = genre.genre.name
                            )
                        }
                    }

                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    enabled = false,
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            }
        },
    )

}