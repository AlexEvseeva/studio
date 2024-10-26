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
        }
    }
    private val _event = MutableSharedFlow<LocationList.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: LocationList.Action) =
        viewModelScope.launch {
            when(action) {
                is LocationList.Action.OnNavigate -> TODO()
            }
        }

    private fun loadLocations(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            locationDataSource.loadLocations(studioId = studioId)
        }
    }
}