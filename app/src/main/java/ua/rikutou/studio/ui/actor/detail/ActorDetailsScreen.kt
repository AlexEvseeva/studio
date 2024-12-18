package ua.rikutou.studio.ui.actor.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.DeleteDialog
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.ImageItem
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.components.dateFormatter
import ua.rikutou.studio.ui.components.yearFormatter

@Composable
fun ActorDetailsScreen(
    viewModel: ActorDetailsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is ActorDetails.Event.OnNavigate -> {
                    if (it.destination == null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(it.destination)
                    }
                }
            }
        }
    }
    ActorDetailsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActorDetailsScreenContent(
    state: ActorDetails.State,
    onAction: (ActorDetails.Action) -> Unit
) {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false
        )
    )

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            DeleteDialog(
                modifier = Modifier.fillMaxWidth(),
                onOk = {
                    onAction(ActorDetails.Action.OnDelete)
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
                    title = state.actor?.entity?.nickName ?: state.actor?.entity?.name ?: "",
                    isEditEnabled = false,
                    onEdit = {
                        onAction(
                            ActorDetails.Action.OnNavigate(
                                destination = Screen.Actor.Edit(
                                    actorId = state.actor?.entity?.actorId
                                )
                            )
                        )
                    }
                )
                ElementContent(
                    label = stringResource(R.string.nameLabel),
                    name = state.actor?.entity?.name ?: ""
                )
                ElementContent(
                    label = stringResource(R.string.nicknameLabel),
                    name = state.actor?.entity?.nickName ?: ""
                )
                ElementContent(
                    label = stringResource(R.string.roleLael),
                    name = state.actor?.entity?.role ?: ""
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.actor?.films?.let { films ->
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 4.dp),
                                text = stringResource(R.string.filesHeader)
                            )
                        }
                        items(items = films) { film ->
                            Item(
                                title = film.title,
                                comment = yearFormatter.format(film.date),
                                onItemClick = {
                                    onAction(
                                        ActorDetails.Action.OnNavigate(
                                            destination = Screen.Film.Details(filmId = film.filmId)
                                        )
                                    )
                                }
                            )
                        }
                    }
                    state.actor?.phones?.let { phones ->
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 4.dp),
                                text = stringResource(R.string.phonesHeader)
                            )
                        }
                        items(items = phones) { item ->
                            Item(
                                title = item.phoneNumber,
                                comment = "",
                                onItemClick = {
                                    with(context) {
                                        val intent = Intent(android.content.Intent.ACTION_DIAL).apply {
                                            data = android.net.Uri.parse("tel:${item.phoneNumber}")
                                        }
                                        if (intent.resolveActivity(packageManager) != null) {
                                            startActivity(intent)
                                        }
                                    }
                                }
                            )
                        }
                    }
                    if(state.actor?.emails?.isNotEmpty() == true) {
                        item {
                            Text(
                                text = stringResource(R.string.emailsHeader)
                            )
                        }
                        items(state.actor.emails) { email ->
                            Item(
                                title = email.email,
                                comment = ""
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
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

@Preview(showSystemUi = true)
@Composable
private fun ActorDetailsScreenPreview() {
    ActorDetailsScreenContent(
        state = ActorDetails.State(),
        onAction = {}
    )
}