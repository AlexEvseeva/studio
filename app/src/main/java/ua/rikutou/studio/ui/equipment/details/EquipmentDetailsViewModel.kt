package ua.rikutou.studio.ui.equipment.details

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
import ua.rikutou.studio.data.datasource.equipment.EquipmentDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class EquipmentDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val equipmentDataSource: EquipmentDataSource,
    private val profileDataSource: ProfileDataSource,
) : ViewModel() {
    private val TAG by lazy { EquipmentDetailsViewModel::class.simpleName }
    private val _state = MutableStateFlow(EquipmentDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadEquipment()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = EquipmentDetails.State()
        )
    private val _event = MutableSharedFlow<EquipmentDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: EquipmentDetails.Action) = viewModelScope.launch {
        when(action) {
            is EquipmentDetails.Action.OnNavigate -> {
                _event.emit(EquipmentDetails.Event.OnNavigate(destination = action.destination))
            }

            EquipmentDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadEquipment() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Equipment.Details>().equipmentId.let { id ->
            profileDataSource.user?.studioId?.let { studioId ->
                equipmentDataSource.getEquipmentById(equipmentId = id, studioId = studioId).collect { equipment ->
                    _state.update {
                        it.copy(equipment = equipment)
                    }
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.equipment?.equipmentId?.let {
            equipmentDataSource.deleteEquipmentById(equipmentId = it)
        }
        _event.emit(EquipmentDetails.Event.OnNavigate())
    }
}