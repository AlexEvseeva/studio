package ua.rikutou.studio.ui.actor.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item

@Composable
fun ActorListScreen(
    viewModel: ActorListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filer by viewModel.filter.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is ActorList.Event.OnNavigate -> {
                    navController.navigate(it.destionation)
                }
            }
        }
    }
    ActorListScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ActorListScreenContent(
    state: ActorList.State,
    onAction: (ActorList.Action) -> Unit
) {
    BackdropScaffold(
        modifier = Modifier
            .background(color = Color(0xFFF5F5F5))
        ,
        scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed),
        frontLayerShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        appBar = {
            ElementTitle(
                modifier = Modifier,
                title = stringResource(R.string.actorsListScreenTitle),
                isSearchEnabled = state.isSearchEnabled,
                isSearchActive = state.isSearchActive,
                onSearch = {
                    onAction(ActorList.Action.OnSearch)
                },
                onCancel = {
                    onAction(ActorList.Action.OnCancel)
                },
                onSearchChanged = {
                    onAction(ActorList.Action.OnSearchChanged(it))
                },
                isAddEnable = false,
                onAdd = {
                    onAction(
                        ActorList.Action.OnNavigate(
                            destionation = Screen.Actor.Edit(
                                actorId = null
                            )
                        )
                    )
                },
                onClearFilters = {
                    onAction(ActorList.Action.OnClearFilters)
                }
            )
        },
        backLayerContent = {

        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(100.dp)
                        .height(6.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))

                )
                LazyColumn(
                    modifier = Modifier.weight(1F),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = state.actors,
                        key = { it.entity.actorId }
                    ) { item ->
                        Item(
                            modifier = Modifier.fillMaxWidth(),
                            title = item.entity.nickName ?: item.entity.name,
                            comment = item.entity.role ?: "",
                            onItemClick = {
                                onAction(
                                    ActorList.Action.OnNavigate(
                                        destionation = Screen.Actor.Details(
                                            actorId = item.entity.actorId
                                        )
                                    )
                                )
                            },
                            onAddToCart = {

                            }
                        )
                    }
                }
            }
        }
    )

}