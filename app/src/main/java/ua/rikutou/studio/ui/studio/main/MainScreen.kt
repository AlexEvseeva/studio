package ua.rikutou.studio.ui.studio.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle(Main.State())

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                is Main.Event.OnNavigate -> {
                    navController.navigate(it.destination)
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
    state: State<Main.State>,
    onAction: (Main.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ElementTitle(title = state.value.studio?.name ?: "") {
            onAction(Main.Action.OnEdit)
        }
        ElementContent(label = "address", name = state.value.studio?.address ?: "")
        ElementContent(label = "postIndex", name = state.value.studio?.postIndex ?: "")
        ElementContent(label = "site", name = state.value.studio?.site ?: "")
        ElementContent(label = "youtube site", name = state.value.studio?.youtube ?: "")
        ElementContent(label = "facebook site", name = state.value.studio?.facebook ?: "")
    }

}

@Composable
@Preview(showSystemUi = true)
private fun MainScreenContentPreview(modifier: Modifier = Modifier) {
    MainScreenContent(state = remember { mutableStateOf(Main.State(studio = StudioEntity(
        studioId = 1,
        name = "Black Crane",
        address = "Kharkiv, Shevchenko st. 27",
        postIndex = "61123",
        site = "https://black.crane.ua",
        youtube = "https://youtube/crane",
        facebook = "https://facebook/crane"
    ))) }) { }
}