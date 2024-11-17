package ua.rikutou.studio.ui.location.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.navigation.Screen
import ua.rikutou.studio.ui.location.details.LocationDetails
import javax.inject.Inject

@HiltViewModel
class LocationEditViewModel
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val savedStateHandle: SavedStateHandle,
    private val userDataSource: UserDataSource
) : ViewModel(){
    private val _state = MutableStateFlow(LocationEdit.State())
    val state = _state.asStateFlow()
        .onStart {
            loadLocation()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LocationEdit.State()
        )
    private val _event = MutableSharedFlow<LocationEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: LocationEdit.Action) = viewModelScope.launch {
        when(action) {
            is LocationEdit.Action.OnFieldChanged -> {
                action.name?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(name = action.name))
                    }
                }
                action.address?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(address = action.address))
                    }
                }
                action.width?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(width = action.width))
                    }
                }
                action.length?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(length = action.length))
                    }
                }
                action.height?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(height = action.height))
                    }
                }
                action.type?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(type = action.type))
                    }
                }
                action.rentPrice?.let {
                    _state.update { s ->
                        s.copy(location = s.location?.copy(rentPrice = action.rentPrice))
                    }
                }
            }
            LocationEdit.Action.OnSave -> onSave()
        }
    }

    private fun loadLocation() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Location.Edit>().locationId?.let { id ->
            locationDataSource.getLocationById(id).collect { location ->
                _state.update {
                    it.copy(location = location.location)
                }
            }
        } ?: run {
            _state.update {
                it.copy(location = LocationEntity(
                    locationId = -1,
                    name = "",
                    address =  "",
                    width = 0F,
                    length = 0F,
                    height = 0F,
                    type = "",
                    studioId = userDataSource.user?.studioId ?: -1,
                    rentPrice = 0F
                ))
            }
        }
    }

    private fun onSave() = viewModelScope.launch {
        if (state.value.location?.name?.isEmpty() == true
            || state.value.location?.address?.isEmpty() == true
            || state.value.location?.type?.isEmpty() == true
            ) {
            return@launch
        }
        state.value.location?.let {
            locationDataSource.save(location = it)
            _event.emit(LocationEdit.Event.OnBack)
        }
    }
}