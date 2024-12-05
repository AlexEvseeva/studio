package ua.rikutou.studio.ui.dept.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.department.DepartmentDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.local.entity.Department
import ua.rikutou.studio.data.local.entity.DepartmentEntity
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class DepartmentEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val departmentDataSource: DepartmentDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(DepartmentEdit.State())
    val state = _state.asStateFlow()
        .onStart {
            loadDepartment()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DepartmentEdit.State()
        )
    private val _event = MutableSharedFlow<DepartmentEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: DepartmentEdit.Action) {
        when(action) {
            is DepartmentEdit.Action.OnFieldChanged -> {
                action.type?.let { type ->
                    _state.value.department?.let { dept ->
                        _state.update {
                            it.copy(
                                department = dept.copy(
                                    entity = dept.entity.copy(
                                        type = type
                                    )
                                )
                            )
                        }
                    }
                }
                action.workHours?.let { wh ->
                    _state.value.department?.let { dept ->
                        _state.update {
                            it.copy(
                                department = dept.copy(
                                    entity = dept.entity.copy(
                                        workHours = wh
                                    )
                                )
                            )
                        }
                    }
                }
                action.contactPerson?.let { cp ->
                    _state.value.department?.let { dept ->
                        _state.update {
                            it.copy(
                                department = dept.copy(
                                    entity = dept.entity.copy(
                                        contactPerson = cp
                                    )
                                )
                            )
                        }
                    }
                }

            }
            DepartmentEdit.Action.OnSave -> onSave()
        }
    }

    private fun loadDepartment() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Department.Edit>().departmentId?.let { id ->
            departmentDataSource.getDepartmentById(id).collect { department ->
                _state.update {
                    it.copy(department = department)
                }
            }
        } ?: run {
            _state.update {
                it.copy(
                    department = Department(
                        entity = DepartmentEntity(
                            departmentId = -1,
                            type = "",
                            workHours = "",
                            contactPerson = "",
                            studioId = profileDataSource.user?.studioId ?: -1,
                        ),
                        sections = emptyList(),
                        transport = emptyList(),
                        phones = emptyList(),
                        emails = emptyList()
                    )
                )
            }
        }
    }

    private fun onSave() = viewModelScope.launch(Dispatchers.IO) {
        if(_state.value.department?.entity?.type?.isEmpty() == true
            || _state.value.department?.entity?.workHours?.isEmpty() == true
            || _state.value.department?.entity?.contactPerson?.isEmpty() == true) {
            return@launch
        }

        _state.value.department?.let {
            departmentDataSource.save(department = it.entity)
            _event.emit(DepartmentEdit.Event.OnBack)
        }
    }

}