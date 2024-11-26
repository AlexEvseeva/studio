package ua.rikutou.studio.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.ui.components.ElementContent
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.UserItem

private const val TAG = "ProfileScreen"

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is Profile.Event.OnNavigate -> {
                    Log.d(TAG, "ProfileScreen: naviage: ${event.route}")
                    navController.navigate(event.route)
                }

                is Profile.Event.OnMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ProfileScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun ProfileScreenContent(
    state: Profile.State,
    onAction: (Profile.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp)
    ) {
        ElementTitle(title = state.user?.name ?: stringResource(R.string.unknown))
        ElementContent(label = stringResource(R.string.userId), name = state.user?.userId.toString())
        ElementContent(label = stringResource(R.string.studioId), name = state.user?.studioId.toString())

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.candidatesList) { user ->
                UserItem(
                    user = user,
                    onCheckChange = {
                        onAction(Profile.Action.OnCheckedChanged(user = user))
                    }
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
            ,
            onClick = {
                onAction(Profile.Action.OnDeleteAccount)
            }
        ) {
            Text(
                text = stringResource(R.string.deleteAccount)
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
            ,
            onClick = {
                onAction(Profile.Action.OnExecute)
            }
        ) {
            Text(
                text = stringResource(R.string.executeSqlQuery)
            )
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        state = Profile.State(),
        onAction = {}
    )

}