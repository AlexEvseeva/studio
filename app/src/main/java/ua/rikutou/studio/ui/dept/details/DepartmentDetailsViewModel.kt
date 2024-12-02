package ua.rikutou.studio.ui.dept.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class DepartmentDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val departmentDataSource: DepartmentDataSource
) : ViewModel() {
    private val TAG by lazy { DepartmentDetailsViewModel::class.simpleName }

    private val _state = MutableStateFlow(DepartmentDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadDepartment()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DepartmentDetails.State()
        )
    private val _event = MutableSharedFlow<DepartmentDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: DepartmentDetails.Action) = viewModelScope.launch {
        when(action) {
            is DepartmentDetails.Action.OnNavigate -> {
                _event.emit(DepartmentDetails.Event.OnNavigate(destination = action.destination))
            }

            DepartmentDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadDepartment() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Department.Details>().departmentId.let { id ->
            departmentDataSource.getDepartmentById(departmentId = id).collect { department ->
                _state.update {
                    it.copy(department = department)
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.department?.entity?.departmentId?.let {
            departmentDataSource.delete(departmentId = it)
        }
        _event.emit(DepartmentDetails.Event.OnNavigate())
    }
}