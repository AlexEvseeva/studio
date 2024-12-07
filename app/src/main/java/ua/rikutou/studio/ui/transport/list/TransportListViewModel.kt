package ua.rikutou.studio.ui.transport.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.transport.TransportDataSource
import ua.rikutou.studio.data.local.entity.TransportEntity
import ua.rikutou.studio.data.remote.transport.TransportType
import ua.rikutou.studio.navigation.Screen
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransportListViewModel @Inject constructor(
    private val transportDataSource: TransportDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(TransportList.State())
    val state = _state
        .onStart {
            profileDataSource.user?.studioId?.let {
                loadTransport(studioId = it)
                getTransport()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )
    
    private val _event = MutableSharedFlow<TransportList.Event>()
    val event = _event.asSharedFlow()

    private val search = MutableStateFlow("")
    val filter = MutableStateFlow(TransportFilter())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                search,
                filter
            ) { search, filter ->
                profileDataSource.user?.studioId?.let { studioId ->
                    if(search.length > 2
                        || filter.byType != null
                        || filter.manufactureDateFrom != null
                        || filter.manufactureDateTo != null
                        || filter.seatsFrom != null
                        || filter.seatsTo != null) {

                        loadTransport(
                            studioId = studioId,
                            search = search,
                            type = filter.byType,
                            manufactureDateFrom = filter.manufactureDateFrom,
                            manufactureDateTo = filter.manufactureDateTo,
                            seatsFrom = filter.seatsFrom,
                            seatsTo = filter.seatsTo
                        )
                    }
                }
            }.collect {}
        }
    }

    fun onAction(action: TransportList.Action) =
        viewModelScope.launch {
            when(action) {
                is TransportList.Action.OnNavigate -> {
                    _event.emit(TransportList.Event.OnNavigate(destionation = action.destionation))
                }
                TransportList.Action.OnCancel -> {
                    _state.update {
                        it.copy(
                            isSearchActive = false,
                            isSearchEnabled = true,
                        )
                    }
                    search.value = ""
                }

                TransportList.Action.OnSearch -> {
                    _state.update {
                        it.copy(
                            isSearchActive = true,
                            isSearchEnabled = false
                        )
                    }
                }
                is TransportList.Action.OnSearchChanged -> {
                    search.value = action.search
                }
                TransportList.Action.OnClearFilters -> {
                    filter.value = TransportFilter()
                }

                is TransportList.Action.OnFieldSelect -> {
                    action.dateFrom?.let { d ->
                        filter.update {
                            it.copy(manufactureDateFrom = d)
                        }
                    }
                    action.dateTo?.let { d ->
                        filter.update {
                            it.copy(manufactureDateTo = d)
                        }
                    }
                    action.seatsFrom?.let { s ->
                        filter.update {
                            it.copy(seatsFrom = s)
                        }
                    }
                    action.seatsTo?.let { s ->
                        filter.update {
                            it.copy(seatsTo = s)
                        }
                    }
                }

                is TransportList.Action.OnTypeSelect -> {
                    filter.update {
                        it.copy(
                            byType = action.type
                        )
                    }
                }
                
                is TransportList.Action.OnAddToCart -> {
                    transportDataSource.addToCart(transportId = action.transportId)
                }
            }
        }

    private fun loadTransport(
        studioId: Long,
        search: String? = null,
        type: TransportType? = null,
        manufactureDateFrom: Date? = null,
        manufactureDateTo: Date? = null,
        seatsFrom: Int? = null,
        seatsTo: Int? = null,
        ) {
        viewModelScope.launch(Dispatchers.IO) {
            transportDataSource.loadTransport(
                studioId = studioId,
                search = search,
                type = type,
                manufactureDateFrom = manufactureDateFrom,
                manufactureDateTo = manufactureDateTo,
                seatsFrom = seatsFrom,
                seatsTo = seatsTo
            )
        }
    }

    private fun getTransport() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                transportDataSource.getTransport(),
                transportDataSource.getSelections(),
                search,
                filter
            ) { list, selectios, search, filter ->
                _state.update {
                    it.copy(
                        transport = if (search.isEmpty()
                            && filter.byType == null
                            && filter.manufactureDateFrom == null
                            && filter.manufactureDateTo == null
                            && filter.seatsFrom == null
                            && filter.seatsTo == null) {
                            list
                        } else {
                            list.filter { transport ->
                                (transport.mark.contains(search, ignoreCase = true) || transport.color.contains(search, ignoreCase = true) || transport.technicalState.contains(search, ignoreCase = true))
                                        && filter.byType?.let { transport.type == it } ?: true
                                        && filter.manufactureDateFrom?.let { transport.manufactureDate.time >= it.time } ?: true
                                        && filter.manufactureDateTo?.let { transport.manufactureDate.time <= it.time } ?: true
                                        && filter.seatsFrom?.let { transport.seats >= it } ?: true
                                        && filter.seatsTo?.let { transport.seats <= it } ?: true
                            }
                        }.map {
                            TransportHolder(transport = it, isSelected = it.transportId in selectios)
                        }
                    )
                }
            }.collect {}
        }
    }

}

data class TransportFilter(
    val byType: TransportType? = null,
    val manufactureDateFrom: Date? = null,
    val manufactureDateTo: Date? = null,
    val seatsFrom: Int? = null,
    val seatsTo: Int? = null
)

data class TransportHolder(
    val transport: TransportEntity,
    val isSelected: Boolean = false,
)