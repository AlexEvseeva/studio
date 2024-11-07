package ua.rikutou.studio.ui.location.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val userDataSource: UserDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(LocationList.State())
    val state = _state.asStateFlow().onStart {
        userDataSource.user?.studioId?.let {
            loadLocations(studioId = it)
            getLocations(studioId = it)
        }
    }
    private val _event = MutableSharedFlow<LocationList.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: LocationList.Action) =
        viewModelScope.launch {
            when(action) {
                is LocationList.Action.OnNavigate -> {
                    _event.emit(LocationList.Event.OnNavigate(destionation = action.destionation))
                }
                LocationList.Action.OnCancel -> {
                    _state.update {
                        it.copy(
                            isSearchActive = false,
                            isSearchEnabled = true
                        )
                    }
                }

                LocationList.Action.OnSearch -> {
                    _state.update {
                        it.copy(
                            isSearchActive = true,
                            isSearchEnabled = false
                        )
                    }
                }
                is LocationList.Action.OnSearchChanged -> TODO()
            }
        }

    private fun loadLocations(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            locationDataSource.loadLocations(studioId = studioId)
        }
    }

    private fun getLocations(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            locationDataSource.getLocationsByStudioId(studioId = studioId)
                .collect { list ->
                    _state.update {
                        it.copy(locations = list)
                    }
                }
        }
    }
}