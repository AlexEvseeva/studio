package ua.rikutou.studio.ui.equipment.edit

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
import ua.rikutou.studio.data.datasource.equipment.EquipmentDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.remote.equipment.EquipmentType
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class EquipmentEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val equipmentDataSource: EquipmentDataSource,
    private val profileDataSource: ProfileDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(EquipmentEdit.State())
    val state = _state.asStateFlow()
        .onStart {
            loadEquipment()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = EquipmentEdit.State()
        )
    private val _event = MutableSharedFlow<EquipmentEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: EquipmentEdit.Action) = viewModelScope.launch {
        when(action) {
            is EquipmentEdit.Action.OnFieldChanged -> {
                action.name?.let {
                    _state.update { s ->
                        s.copy(equipment = s.equipment?.copy(name = action.name))
                    }
                }
                action.comment?.let {
                    _state.update { s ->
                        s.copy(equipment = s.equipment?.copy(comment = action.comment))
                    }
                }
                action.rentPrice?.let {
                    _state.update { s ->
                        s.copy(equipment = s.equipment?.copy(rentPrice = action.rentPrice))
                    }
                }
            }
            EquipmentEdit.Action.OnSave -> onSave()
            is EquipmentEdit.Action.OnTypeSelected -> {
                _state.update { s ->
                    s.copy(equipment = s.equipment?.copy(type = action.type))
                }
            }
        }
    }

    private fun loadEquipment() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Equipment.Edit>().equipmentId?.let { id ->
            profileDataSource.user?.studioId?.let { studioId ->
                equipmentDataSource.getEquipmentById(equipmentId = id, studioId = studioId).collect { equipment ->
                    _state.update {
                        it.copy(equipment = equipment)
                    }
                }
            }
        } ?: run {
            _state.update {
                it.copy(equipment = EquipmentEntity(
                    equipmentId = -1,
                    name = "",
                    type = EquipmentType.Camera,
                    comment = "",
                    rentPrice = 0F,
                    studioId = profileDataSource.user?.studioId ?: -1L
                )
                )
            }
        }
    }

    private fun onSave() = viewModelScope.launch {
        if (state.value.equipment?.name?.isEmpty() == true
            || state.value.equipment?.comment?.isEmpty() == true
            || state.value.equipment?.rentPrice == 0F
        ) {
            return@launch
        }
        state.value.equipment?.let {
            equipmentDataSource.updateEquipment(equipment = it)
            _event.emit(EquipmentEdit.Event.OnBack)
        }
    }
}