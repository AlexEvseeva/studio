package ua.rikutou.studio.ui.document

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import ua.rikutou.studio.R

@Composable
fun DocumentCreateScreen(
    viewModel: DocumentCreateViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {

        }
    }
    DocumentCreateScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun DocumentCreateScreenContent(
    state: DocumentCreate.State,
    onAction: (DocumentCreate.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.documentHeader),
            fontSize = 20.sp,
            fontWeight = FontWeight(700)
        )
        Text(
            text = stringResource(R.string.documentMessage),
            fontSize = 12.sp,
            fontWeight = FontWeight(400)
        )
        if(state.locations.isNotEmpty()) {
            Text(
                text = stringResource(R.string.documentLocationsLabel),
                fontSize = 16.sp,
                fontWeight = FontWeight(700)
            )
            state.locations.forEachIndexed { index, location ->
                Text(
                    text = "${index+1} ${location.location.name}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700)
                )
                Text(
                    text = location.location.address,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400)
                )
            }
            Text(
                text = "${stringResource(R.string.subtotal)} ${state.locationSum}",
                fontSize = 14.sp,
                fontWeight = FontWeight(700)
            )

        }
    }


}