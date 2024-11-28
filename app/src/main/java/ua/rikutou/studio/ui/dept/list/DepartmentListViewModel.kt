package ua.rikutou.studio.ui.dept.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.department.DepartmentDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import javax.inject.Inject

@HiltViewModel
class DepartmentListViewModel @Inject constructor(
    private val departmentDataSource: DepartmentDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(Department.State())
    val state = _state
        .onStart {
            profileDataSource.user?.studioId?.let {
                loadDepartments(studioId = it, search = "")
                observeDepartments(studioId = it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    private val _event = MutableSharedFlow<Department.Event>()
    val event = _event.asSharedFlow()

    private val search = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            search
                .collect { search ->
                    if (search.length > 2) {
                        profileDataSource.user?.studioId?.let { id ->
                            loadDepartments(
                                studioId = id,
                                search = search
                            )
                        }
                    }
                }
        }
    }

    fun onAction(action: Department.Action) = viewModelScope.launch {
        when(action) {
            Department.Action.OnCancel -> {
                _state.update {
                    it.copy(
                        isSearchActive = false,
                        isSearchEnabled = true,
                    )
                }
                search.value = ""
            }
            is Department.Action.OnNavigate -> {
                _event.emit(Department.Event.OnNavigate(destionation = action.destionation))
            }
            Department.Action.OnSearch -> {
                _state.update {
                    it.copy(
                        isSearchActive = true,
                        isSearchEnabled = false
                    )
                }
            }
            is Department.Action.OnSearchChanged -> {
                search.value = action.search
            }
        }

    }

    private fun loadDepartments(studioId: Long, search: String) = viewModelScope.launch {
        departmentDataSource.loadDepartments(
            studioId = studioId,
            search = search
        )
    }

    private fun observeDepartments(studioId: Long) = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                departmentDataSource.getAllDepartments(studioId),
                search
            ) { list, search ->
                _state.update {
                    it.copy(
                        departments = if(search.isEmpty()) {
                            list.map { it.entity }
                        } else {
                            list.map { it.entity }.filter { deptEntity ->
                                deptEntity.type.contains(search, ignoreCase = true)
                            }
                        }
                    )
                }
            }.collect {}
        }
    }
}