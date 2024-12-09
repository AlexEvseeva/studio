package ua.rikutou.studio.ui.document

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.document.DocumentDataSource
import ua.rikutou.studio.data.datasource.equipment.EquipmentDataSource
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import ua.rikutou.studio.data.datasource.transport.TransportDataSource
import ua.rikutou.studio.navigation.Screen
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DocumentCreateViewModel @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val profileDataSource: ProfileDataSource,
    private val studioDataSource: StudioDataSource,
    private val transportDataSource: TransportDataSource,
    private val equipmentDataSource: EquipmentDataSource,
    private val documentDataSource: DocumentDataSource
) : ViewModel() {
    private val TAG by lazy { DocumentCreateViewModel::class.simpleName }
    private val _state = MutableStateFlow(DocumentCreate.State())
    val state = _state.onStart {
        profileDataSource.user?.studioId?.let { studioId ->
            getSelectedLocations(studioId = studioId)
            getStudio(studioId = studioId)
            getSelectedTransport()
            getSelectedEquipment(studioId = studioId)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _state.value
    )

    private val _event = MutableSharedFlow<DocumentCreate.Event>()
    val event = _event.asSharedFlow()

    fun onAction(actin: DocumentCreate.Action) = viewModelScope.launch {
        when(actin) {
            is DocumentCreate.Action.OnNavigate -> {
                _event.emit(DocumentCreate.Event.OnNavigate(destination = actin.destination))
            }
            is DocumentCreate.Action.OnRemoveEquipmentFromCart -> {
                equipmentDataSource.removeFromCart(listOf(actin.equipmentId))
            }
            is DocumentCreate.Action.OnRemoveLocationFromCart -> {
                locationDataSource.removeFromCart(listOf(actin.locationId))
            }
            is DocumentCreate.Action.OnRemoveTransportFromCart -> {
                transportDataSource.removeFromCart(listOf(actin.transportId))
            }

            is DocumentCreate.Action.OnSelectFromDate -> {
                val currentDate = Date().time
                if(actin.time >= currentDate) {
                    _state.update {
                        it.copy(fromDate = Date(actin.time))
                    }
                } else {
                    _event.emit(DocumentCreate.Event.OnMessage(message = "Date in past not allowed"))
                }
            }
            is DocumentCreate.Action.OnSelectToDays -> {
                _state.update {
                    it.copy(toDays = when {
                        actin.days < 0 -> actin.days * -1
                        actin.days == 0 -> 1
                        else -> actin.days
                    })
                }
            }

            DocumentCreate.Action.OnCreateDocument -> createDocument()
        }

    }

    private fun getSelectedLocations(studioId: Long) = viewModelScope.launch {
        combine(
            locationDataSource.getLocationsSelection(),
            locationDataSource.getLocationsByStudioId(studioId = studioId)
        ) { selected, list ->
            val filteredLocations = list.filter { it.location.locationId in selected }
            _state.update {
                it.copy(
                    locations = filteredLocations,
                    locationSum = filteredLocations.fold(initial = 0F) { acc, location ->
                        acc + location.location.rentPrice
                    }
                )
            }
        }.collect()
    }

    private fun getSelectedTransport() = viewModelScope.launch {
        combine(
            transportDataSource.getTransport(),
            transportDataSource.getSelections()
        ) { list, selection ->
            val filtered = list.filter { it.transportId in selection }
            _state.update {
                it.copy(
                    transport = filtered,
                )
            }
        }.collect()
    }

    private fun getSelectedEquipment(studioId: Long) = viewModelScope.launch {
        combine(
            equipmentDataSource.getAllEquipment(studioId = studioId),
            equipmentDataSource.getAllSelected()
        ) { list, selected ->
            val filterd = list.filter { it.equipmentId in selected }
            _state.update {
                it.copy(
                    equipment = filterd,
                    equipmentSum = filterd.fold(initial = 0F) { acc, equipment ->
                        acc + equipment.rentPrice
                    }
                )
            }
        }.collect()
    }

    private fun getStudio(studioId: Long) = viewModelScope.launch {
        studioDataSource.getStudioById(studioId = studioId).collect { studio ->
            _state.update {
                it.copy(
                    studio = studio
                )
            }
        }
    }

    private fun createDocument() = viewModelScope.launch {
        documentDataSource.createDocument(
            dataStart = state.value.fromDate ?: Date(),
            days = state.value.toDays,
            locations = state.value.locations.map { it.location.locationId },
            transport = state.value.transport.map { it.transportId },
            equipment = state.value.equipment.map { it.equipmentId }
        )
        _event.emit(DocumentCreate.Event.OnNavigate(destination = Screen.Studio.Main))
    }
}