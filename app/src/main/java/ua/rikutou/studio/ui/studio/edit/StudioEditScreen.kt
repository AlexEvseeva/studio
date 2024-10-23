package ua.rikutou.studio.ui.studio.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun StudioEditScreen(
    viewModel: StudioEditViewModel,
    navController: NavController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {

        }
    }
    StudioEditScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun StudioEditScreenContent(
    state: State<StudioEdit.State>,
    onAction: (StudioEdit.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Studio edit: ${state.value.studioId}")
    }
}

@Composable
@Preview(showSystemUi = true)
fun StudioEditScreenContentPreview() {
    StudioEditScreenContent(state = remember { mutableStateOf(StudioEdit.State()) }) { }
}