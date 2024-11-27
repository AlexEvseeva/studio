package ua.rikutou.studio.ui.dept

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.rikutou.studio.R
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.components.ElementTitle
import ua.rikutou.studio.ui.components.Item
import ua.rikutou.studio.ui.location.list.LocationList

@Composable
fun DepartmentListScreen(
    viewModel: DepartmentListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when(event) {
                is Department.Event.OnNavigate -> TODO()
            }
        }
    }

    DepartmentListScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
private fun DepartmentListScreenContent(
    state: Department.State,
    onAction: (Department.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        ElementTitle(
            modifier = Modifier,
            title = stringResource(R.string.departmentTitle),
            isSearchEnabled = state.isSearchEnabled,
            isSearchActive = state.isSearchActive,
            onSearch = {
                onAction(Department.Action.OnSearch)
            },
            onCancel = {
                onAction(Department.Action.OnCancel)
            },
            onSearchChanged = {
                onAction(Department.Action.OnSearchChanged(it))
            },
            onAdd = {
                onAction(
                    Department.Action.OnNavigate(
                        destionation = Screen.Department.Edit(
                            departmentId = null
                        )
                    )
                )
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = state.departments,
                key = { it.departmentId }
            ) { item ->
                Item(
                    modifier = Modifier.fillMaxWidth(),
                    title = item.type,
                    comment = item.workHours
                ) {
                    onAction(Department.Action.OnNavigate(Screen.Department.Details(departmentId = item.departmentId)))
                }
            }
        }
        
    }
    
}

