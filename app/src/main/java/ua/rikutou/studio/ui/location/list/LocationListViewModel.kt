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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.remote.location.LocationType
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class LocationListViewModel
@Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {
    private val TAG by lazy { LocationListViewModel::class.simpleName }

    private val _state = MutableStateFlow(LocationList.State())
    val state = _state.asStateFlow().onStart {
        profileDataSource.user?.studioId?.let {
            loadLocations(studioId = it, search = "")
            getLocations(studioId = it)
        }
    }
    private val _event = MutableSharedFlow<LocationList.Event>()
    val event = _event.asSharedFlow()

    private val search = MutableStateFlow("")
    val filter = MutableStateFlow(LocationFilter())
    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                search,
                filter
            ) { search, filter ->
                profileDataSource.user?.studioId?.let { studioId ->
                    if(search.length > 2 || filter.byType != null || filter.dimensions != null) {
                        loadLocations(
                            studioId = studioId,
                            search = search,
                            type = filter.byType,
                            widthFrom = filter.dimensions?.widthFrom,
                            widthTo = filter.dimensions?.widthTo,
                            lengthFrom = filter.dimensions?.lengthFrom,
                            lengthTo = filter.dimensions?.lengthTo,
                            heightFrom = filter.dimensions?.heightFrom,
                            heightTo = filter.dimensions?.heightTo
                        )
                    }
                }
            }.collect {}
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

                is LocationList.Action.OnTypeSelect -> {
                    filter.update {
                        it.copy(
                            byType = action.type
                        )
                    }
                }

                LocationList.Action.OnClearFilters -> {
                    filter.value = LocationFilter()
                }

                is LocationList.Action.OnDimansionsChange -> {
                    filter.emit(
                        filter.value.copy(
                            dimensions = action.dimensions
                        )
                    )
                }
            }
        }

    private fun loadLocations(
        studioId: Long,
        search: String,
        type: LocationType? = null,
        widthFrom: Int? = null,
        widthTo: Int? = null,
        lengthFrom: Int? = null,
        lengthTo: Int? = null,
        heightFrom: Int? = null,
        heightTo: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            locationDataSource.loadLocations(
                studioId = studioId,
                type = type,
                search = search,
                widthFrom= widthFrom,
                widthTo = widthTo,
                lengthFrom = lengthFrom,
                lengthTo = lengthTo,
                heightFrom = heightFrom,
                heightTo = heightTo
            )
        }
    }

    private fun getLocations(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                locationDataSource.getLocationsByStudioId(studioId = studioId),
                search,
                filter
                ) { list, search, filter ->
                _state.update {
                    it.copy(
                        locations = if (search.isEmpty() && filter.byType == null && filter.dimensions == null) {
                            list
                        } else {
                            list.filter { location ->
                                (location.location.name.contains(search, ignoreCase = true) || location.location.address.contains(search, ignoreCase = true))
                                        && (filter.byType?.let { location.location.type == it } ?: true)
                                        && (filter.dimensions?.let { d ->
                                            d.widthFrom?.let { location.location.width >= it } ?: true
                                                    && d.widthTo?.let { location.location.width <= it } ?: true
                                                    && d.lengthFrom?.let { location.location.length >= it } ?: true
                                                    && d.lengthTo?.let { location.location.length <= it } ?: true
                                                    && d.heightFrom?.let { location.location.height >= it} ?: true
                                                    && d.heightTo?.let { location.location.height <= it } ?: true
                                        } ?: true)
                            }
                        }
                    )
                }
            }.collect {}
        }
    }
}

data class LocationFilter(
    val byType: LocationType? = null,
    val dimensions: Dimensions? = null
)

data class Dimensions(
    val widthFrom: Int? = null,
    val widthTo: Int? = null,
    val lengthFrom: Int? = null,
    val lengthTo: Int? = null,
    val heightFrom: Int? = null,
    val heightTo: Int? = null,
)