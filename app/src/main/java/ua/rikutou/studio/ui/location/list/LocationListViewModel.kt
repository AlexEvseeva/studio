package ua.rikutou.studio.ui.location.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class LocationListViewModel
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val userDataSource: UserDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(LocationList.State())
    val state = _state.asStateFlow().onStart {
        userDataSource.user?.studioId?.let {
            loadLocations(studioId = it, search = "")
            getLocations(studioId = it)
        }
    }
    private val _event = MutableSharedFlow<LocationList.Event>()
    val event = _event.asSharedFlow()

    private val search = MutableStateFlow("")
    init {
        viewModelScope.launch(Dispatchers.IO) {
            search
                .collect { search ->
                    if (search.length > 2) {
                        userDataSource.user?.studioId?.let { id ->
                            loadLocations(
                                studioId = id,
                                search = search
                            )
                        }
                    }
            }
        }
    }

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
                            isSearchEnabled = true,
                        )
                    }
                    search.value = ""
                }

                LocationList.Action.OnSearch -> {
                    _state.update {
                        it.copy(
                            isSearchActive = true,
                            isSearchEnabled = false
                        )
                    }
                }
                is LocationList.Action.OnSearchChanged -> {
                    search.value = action.search
                }
            }
        }

    private fun loadLocations(studioId: Long, search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            locationDataSource.loadLocations(studioId = studioId, search = search)
        }
    }

    private fun getLocations(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                locationDataSource.getLocationsByStudioId(studioId = studioId),
                search
                ) { list, search ->
                _state.update {
                    it.copy(
                        locations = if (search.isEmpty()) {
                            list
                        } else {
                            list.filter { location ->
                                location.location.name.contains(search,ignoreCase = true)
                                        || location.location.address.contains(search,ignoreCase = true)

                            }
                        }
                    )
                }
            }.collect {}

//            locationDataSource.getLocationsByStudioId(studioId = studioId)
//                .collect { list ->
//                    _state.update {
//                        it.copy(locations = list)
//                    }
//                }
        }
    }
}