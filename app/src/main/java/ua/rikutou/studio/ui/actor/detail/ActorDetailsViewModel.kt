package ua.rikutou.studio.ui.actor.detail

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
import ua.rikutou.studio.data.datasource.actor.ActorDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val actorDataSource: ActorDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(ActorDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadLocation()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ActorDetails.State()
        )
    private val _event = MutableSharedFlow<ActorDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: ActorDetails.Action) = viewModelScope.launch {
        when(action) {
            is ActorDetails.Action.OnNavigate -> {
                _event.emit(ActorDetails.Event.OnNavigate(destination = action.destination))
            }

            ActorDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadLocation() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Actor.Details>().actorId.let { id ->
            actorDataSource.getById(actorId = id).collect { item ->
                _state.update {
                    it.copy(actor = item)
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.actor?.entity?.actorId?.let {
            actorDataSource.delete(actorId = it)
        }
        _event.emit(ActorDetails.Event.OnNavigate())
    }
    
}