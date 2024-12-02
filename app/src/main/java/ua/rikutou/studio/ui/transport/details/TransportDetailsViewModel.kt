package ua.rikutou.studio.ui.transport.details

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
import ua.rikutou.studio.data.datasource.transport.TransportDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class TransportDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transportDataSource: TransportDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(TransportDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadLocation()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TransportDetails.State()
        )
    private val _event = MutableSharedFlow<TransportDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: TransportDetails.Action) = viewModelScope.launch {
        when(action) {
            is TransportDetails.Action.OnNavigate -> {
                _event.emit(TransportDetails.Event.OnNavigate(destination = action.destination))
            }

            TransportDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadLocation() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Transport.Details>().transportId?.let { id ->
            transportDataSource.getTransportById(transportId = id).collect { transport ->
                _state.update {
                    it.copy(transport = transport)
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.transport?.transportId?.let {
            transportDataSource.delete(transportId = it)
        }
        _event.emit(TransportDetails.Event.OnNavigate())
    }
}