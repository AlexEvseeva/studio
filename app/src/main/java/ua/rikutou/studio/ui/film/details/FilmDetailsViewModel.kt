package ua.rikutou.studio.ui.film.details

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
import ua.rikutou.studio.data.datasource.film.FilmDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class FilmDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val filmDataSource: FilmDataSource,

) : ViewModel() {
    private val _state = MutableStateFlow(FilmDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadFilm()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FilmDetails.State()
        )
    private val _event = MutableSharedFlow<FilmDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: FilmDetails.Action) = viewModelScope.launch {
        when(action) {
            is FilmDetails.Action.OnNavigate -> {
                _event.emit(FilmDetails.Event.OnNavigate(destination = action.destination))
            }

            FilmDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadFilm() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Film.Details>().filmId.let { id ->
            filmDataSource.getById(filmId = id).collect { item ->
                _state.update {
                    it.copy(film = item)
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.film?.entity?.filmId?.let {
//            filmDataSource.delete(actorId = it)
        }
        _event.emit(FilmDetails.Event.OnNavigate())
    }
}