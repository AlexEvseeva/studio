package ua.rikutou.studio.ui.section.edit

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
import ua.rikutou.studio.data.datasource.section.SectionDataSource
import ua.rikutou.studio.data.local.entity.SectionEntity
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SectionEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sectionDataSource: SectionDataSource,
    private val departmentDataSource: DepartmentDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {
    private val TAG by lazy { SectionEditViewModel::class.simpleName }

    private val _state = MutableStateFlow(SectionEdit.State())
    val state = _state.asStateFlow()
        .onStart {
            getSection()
            getDepartments()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )
    private val _event = MutableSharedFlow<SectionEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: SectionEdit.Action) = viewModelScope.launch {
        when(action) {
            is SectionEdit.Action.OnFieldChanged -> {
                action.title?.let { act ->
                    _state.update { s ->
                        s.copy(
                            section = s.section?.copy(
                                title = act
                            )
                        )
                    }
                }

                action.address?.let { act ->
                    _state.update { s ->
                        s.copy(
                            section = s.section?.copy(
                                address = act
                            )
                        )
                    }
                }

                action.internalPhoneNumber?.let { act ->
                    _state.update { s ->
                        s.copy(
                            section = s.section?.copy(
                                internalPhoneNumber = act
                            )
                        )
                    }
                }
            }
            SectionEdit.Action.OnSave -> onSave()
            is SectionEdit.Action.OnDepartmentSelect -> {
                _state.update {
                    it.copy(
                        section = it.section?.copy(
                            departmentId = action.departmentId
                        )
                    )
                }
            }
        }
    }

    private fun getSection() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Section.Edit>().sectionId?.let { id ->
            sectionDataSource.getSectionById(id).collect { section ->
                _state.update {
                    it.copy(section = section)
                }
            }
        } ?: run {
            _state.update {
                it.copy(
                    section = SectionEntity(
                        sectionId = -1L,
                        title = "",
                        address = "",
                        internalPhoneNumber = "",
                        departmentId = -1L,
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
                        section = if((s.section?.departmentId ?: -1L) < 0) {
                            s.section?.copy(departmentId = list.first().entity.departmentId)
                        } else {
                            s.section
                        }
                    )
                }
            }
        }
    }

    private fun onSave() = viewModelScope.launch {
        if(state.value.section?.title?.isEmpty() == true
            || state.value.section?.address?.isEmpty() == true
            || state.value.section?.internalPhoneNumber?.isEmpty() == true) {
            return@launch
        }
        state.value.section?.let {
            sectionDataSource.createUpdateSection(sectionEntity = it)
            _event.emit(SectionEdit.Event.OnBack)
        }
    }
}