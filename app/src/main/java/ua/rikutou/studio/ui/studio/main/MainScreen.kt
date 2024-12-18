package ua.rikutou.studio.ui.studio.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle
import java.lang.StringBuilder

private val TAG by lazy { "MainScreen" }

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle(Main.State())
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is Main.Event.OnNavigate -> {
                    navController.navigate(it.destination)
                }

                is Main.Event.OnMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    MainScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
    
}

@Composable
private fun MainScreenContent(
    state: Main.State,
    onAction: (Main.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp)
            .verticalScroll(state = rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if(state.studio == null) {
            Button(
                onClick = {
                    onAction(Main.Action.OnEdit)
                }
            ) {
                Text(text = stringResource(R.string.addStudio))
            }
        } else {
            ElementTitle(
                title = state.studio?.name ?: "",
                isEditEnabled = true,
                onEdit = {
                    onAction(Main.Action.OnEdit)
                },
                isCartEnable = true,
                onCart = {
                    onAction(Main.Action.OnNavigate(
                        destination = Screen.Document.Create)
                    )
                }
            )
            ElementContent(label = "address", name = state.studio?.address ?: "")
            ElementContent(label = "postIndex", name = state.studio?.postIndex ?: "")
            ElementContent(label = "site", name = state.studio?.site ?: "")
            ElementContent(label = "youtube site", name = state.studio?.youtube ?: "")
            ElementContent(label = "facebook site", name = state.studio?.facebook ?: "")

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color.Black)
                ,
            )
            if(state.statistic != null)  {
                with(state.statistic) {
                    location?.let { item ->
                        ElementContent(
                            label = stringResource(R.string.statByLocations),
                            name = String.format(stringResource(R.string.minAvgMax), item.min, item.avg, item.max)
                        )
                    }
                    transport?.let { item ->
                        ElementContent(
                            label = stringResource(R.string.statByTransport),
                            name = String.format(stringResource(R.string.minAvgMax), item.min, item.avg, item.max)
                        )
                    }
                    equipment?.let { item ->
                        ElementContent(
                            label = stringResource(R.string.statByEquipment),
                            name = String.format(stringResource(R.string.minAvgMax), item.min, item.avg, item.max)
                        )
                    }
                    incomeStructure?.let { income ->
                        ElementContent(
                            label = stringResource(R.string.incomeStructure),
                            name = String
                                .format(
                                    stringResource(R.string.incomeStructureMessage),
                                    income.byLocation ?: 0F,
                                    income.byTransport ?: 0F,
                                    income.byEquipment ?: 0F,
                                    income.total ?: 0F
                                )
                        )
                    }
                    documentsTotal?.let {
                        ElementContent(
                            label = stringResource(R.string.totalDocuments),
                            name = it.toString()
                        )
                    }
                    if(mostPopularLocations?.isNotEmpty() == true) {
                        ElementContent(
                            label = stringResource(R.string.mostPopularLocation),
                            name = mostPopularLocations.first().name
                        )
                    }
                    if(mostPopularTransport?.isNotEmpty() == true) {
                        ElementContent(
                            label = stringResource(R.string.mostPopularTransport),
                            name = mostPopularTransport.first().mark
                        )
                    }
                    if(mostPopularEquipment?.isNotEmpty() == true) {
                        ElementContent(
                            label = stringResource(R.string.mostPopularEquipment),
                            name = mostPopularEquipment.first().name
                        )
                    }
                    mostPopularActor?.let { actor ->
                        ElementContent(
                            label = stringResource(R.string.mostPupularActor),
                            name = actor.nickName ?: actor.name ?: ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun MainScreenContentPreview(modifier: Modifier = Modifier) {
    MainScreenContent(state = Main.State(studio = StudioEntity(
        studioId = 1,
        name = "Black Crane",
        address = "Kharkiv, Shevchenko st. 27",
        postIndex = "61123",
        site = "https://black.crane.ua",
        youtube = "https://youtube/crane",
        facebook = "https://facebook/crane"
    ))) {}
}