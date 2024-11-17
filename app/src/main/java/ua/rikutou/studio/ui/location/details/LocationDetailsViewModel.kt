package ua.rikutou.studio.ui.location.details

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
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _state = MutableStateFlow(LocationDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadLocation()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LocationDetails.State()
        )
    private val _event = MutableSharedFlow<LocationDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: LocationDetails.Action) = viewModelScope.launch {
        when(action) {
            is LocationDetails.Action.OnNavigate -> {
                _event.emit(LocationDetails.Event.OnNavigate(destination = action.destination))
            }

            LocationDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadLocation() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Location.Details>().locationId?.let { id ->
            locationDataSource.getLocationById(locationId = id).collect { location ->
                _state.update {
                    it.copy(location = location)
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.location?.location?.locationId?.let {
            locationDataSource.delete(locationId = it)
        }
        _event.emit(LocationDetails.Event.OnNavigate())
    }
}