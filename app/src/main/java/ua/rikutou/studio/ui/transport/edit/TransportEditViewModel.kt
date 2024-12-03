package ua.rikutou.studio.ui.transport.edit

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
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.transport.TransportDataSource
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.navigation.Screen
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransportEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transportDataSource: TransportDataSource,
    private val profileDataSource: ProfileDataSource,
    private val departmentDataSource: DepartmentDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(TransportEdit.State())
    val state = _state.asStateFlow()
        .onStart {
            getTransport()
            getDepartments()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TransportEdit.State()
        )
    private val _event = MutableSharedFlow<TransportEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: TransportEdit.Action) = viewModelScope.launch {
        when(action) {
            is TransportEdit.Action.OnFieldChanged -> {
                action.type?.let { type ->
                    _state.update { s ->
                        s.copy(
                            transport = s.transport?.copy(
                                type = type.runCatching { TransportType.valueOf(type) }.getOrNull() ?: TransportType.Sedan
                            )
                        )
                    }
                }
                action.mark?.let { mark ->
                    _state.update { s ->
                        s.copy(
                            transport = s.transport?.copy(
                                mark = mark
                            )
                        )
                    }
                }
                action.manufactureDate?.let { date ->
                    _state.update { s ->
                        s.copy(
                            transport = s.transport?.copy(
                                manufactureDate = date,
                            ),
                            isSelectDateDialogActive = false
                        )
                    }
                }
                action.seats?.let { seats ->
                    _state.update { s ->
                        s.copy(
                            transport = s.transport?.copy(
                                seats = seats
                            )
                        )
                    }
                }
                action.color?.let { color ->
                    _state.update { s ->
                        s.copy(
                            transport = s.transport?.copy(
                                color = color
                            )
                        )
                    }
                }
                action.technicalState?.let { state ->
                    _state.update { s ->
                        s.copy(
                            transport = s.transport?.copy(
                                technicalState = state
                            )
                        )
                    }
                }

            }
            TransportEdit.Action.OnSave -> onSave()
            TransportEdit.Action.OnSelectDate -> {
                _state.update {
                    it.copy(isSelectDateDialogActive = true)
                }
            }

            is TransportEdit.Action.OnDepartmentSelect -> {
                _state.update { s ->
                    s.copy(
                        transport = s.transport?.copy(
                            departmentId = action.departmentId
                        )
                    )
                }
            }

            TransportEdit.Action.OnDismissDatePicker -> {
                _state.update {
                    it.copy(
                        isSelectDateDialogActive = false
                    )
                }
            }
        }
    }

    private fun getTransport() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Transport.Edit>().transportId?.let { id ->
            transportDataSource.getTransportById(transportId = id).collect { transport ->
                _state.update {
                    it.copy(transport = transport)
                }
            }
        } ?: run {
            _state.update {
                it.copy(transport = TransportEntity(
                    transportId = -1L,
                    type = TransportType.Sedan,
                    mark = "",
                    manufactureDate = Date(),
                    seats = 0,
                    departmentId = -1L,
                    color = "",
                    technicalState = "",
                )
                )
            }
        }
    }

    private fun getDepartments() = viewModelScope.launch {
        profileDataSource.user?.studioId?.let { studioId ->
            departmentDataSource.getAllDepartments(studioId = studioId).collect { list ->
                _state.update { s ->
                    s.copy(
                        departments = list.map { it.entity },
                        transport = if((s.transport?.departmentId ?: -1L) < 0) {
                            s.transport?.copy(departmentId = list.first().entity.departmentId)
                        } else {
                            s.transport
                        }
                    )
                }
            }
        }
    }

    private fun onSave() = viewModelScope.launch {
        if (state.value.transport?.type != null
            || state.value.transport?.mark?.isEmpty() == true
            || state.value.transport?.seats == 0
            || state.value.transport?.color?.isEmpty() == true
            || state.value.transport?.technicalState?.isEmpty() == true
        ) {
            return@launch
        }
        state.value.transport?.let {
            transportDataSource.save(transport = it)
            _event.emit(TransportEdit.Event.OnBack)
        }
    }

}