package ua.rikutou.studio.ui.location.edit

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
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.local.entity.LocationEntity
import ua.rikutou.studio.data.remote.location.LocationType
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class LocationEditViewModel
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val savedStateHandle: SavedStateHandle,
    private val profileDataSource: ProfileDataSource
) : ViewModel(){
    private val TAG by lazy { LocationEditViewModel::class.simpleName }

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
                action.width?.let { width ->
                    val w = width.runCatching { width.toFloat() }.getOrNull() ?: 0F
                    _state.update { s ->
                        s.copy(location = s.location?.copy(width = if(w >= 0F) w else -1 * w ))
                    }
                }
                action.length?.let { length ->
                    val l = length.runCatching { length.toFloat() }.getOrNull() ?: 0F
                    _state.update { s ->
                        s.copy(location = s.location?.copy(length = if(l >= 0F) l else -1 * l))
                    }
                }
                action.height?.let { height ->
                    val h = height.runCatching { height.toFloat() }.getOrNull() ?: 0F
                    _state.update { s ->
                        s.copy(location = s.location?.copy(height = if(h >= 0F) h else -1 * h))
                    }
                }
                action.rentPrice?.let { price ->
                    val p = price.runCatching { price.toFloat() }.getOrNull() ?: 0F
                    _state.update { s ->
                        s.copy(location = s.location?.copy(rentPrice = if(p >= 0F) p else -1 * p))
                    }
                }
            }
            LocationEdit.Action.OnSave -> onSave()
            is LocationEdit.Action.OnTypeSelected -> {
                Log.d(TAG, "onAction: ${action.option}")
                _state.update { s ->
                    s.copy(
                        location = s.location?.copy(
                            type = LocationType.valueOf(action.option)
                        )
                    )
                }
            }
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
                    type = LocationType.pavilion,
                    studioId = profileDataSource.user?.studioId ?: -1,
                    rentPrice = 0F
                ))
            }
        }
    }

    private fun onSave() = viewModelScope.launch {
        if (state.value.location?.name?.isEmpty() == true
            || state.value.location?.address?.isEmpty() == true
            ) {
            return@launch
        }
        state.value.location?.let {
            locationDataSource.save(location = it)
            _event.emit(LocationEdit.Event.OnBack)
        }
    }
}