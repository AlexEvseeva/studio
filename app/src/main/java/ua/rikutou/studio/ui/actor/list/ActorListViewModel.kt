package ua.rikutou.studio.ui.actor.list

import android.util.Log
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
import ua.rikutou.studio.data.datasource.actor.ActorDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import javax.inject.Inject

@HiltViewModel
class ActorListViewModel @Inject constructor(
    private val actorDataSource: ActorDataSource,
    private val profileDataSource: ProfileDataSource
) : ViewModel() {
    private val TAG by lazy { ActorListViewModel::class.simpleName }

    private val _state = MutableStateFlow(ActorList.State())
    val state = _state
        .onStart {
            profileDataSource.user?.studioId?.let {
                loadActors(studioId = it, search = "")
                getActors(studioId = it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )
    private val _event = MutableSharedFlow<ActorList.Event>()
    val event = _event.asSharedFlow()

    private val search = MutableStateFlow("")
    val filter = MutableStateFlow(ActorFilter())
    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                search,
                filter
            ) { search, filter ->
                profileDataSource.user?.studioId?.let { studioId ->
                    if(search.length > 2 || filter.dummy != null) {
                        loadActors(
                            studioId = studioId,
                            search = search,
                        )
                    }
                }
            }.collect {}
        }
    }

    fun onAction(action: ActorList.Action) =
        viewModelScope.launch {
            when(action) {
                is ActorList.Action.OnNavigate -> {
                    _event.emit(ActorList.Event.OnNavigate(destionation = action.destionation))
                }
                ActorList.Action.OnCancel -> {
                    _state.update {
                        it.copy(
                            isSearchActive = false,
                            isSearchEnabled = true,
                        )
                    }
                    search.value = ""
                }

                ActorList.Action.OnSearch -> {
                    _state.update {
                        it.copy(
                            isSearchActive = true,
                            isSearchEnabled = false
                        )
                    }
                }
                is ActorList.Action.OnSearchChanged -> {
                    search.value = action.search
                }


                ActorList.Action.OnClearFilters -> {
                    filter.value = ActorFilter()
                }
            }
        }

    private fun loadActors(
        studioId: Long,
        search: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            actorDataSource.load(
                studioId = studioId,
                search = search,
            )
        }
    }

    private fun getActors(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                actorDataSource.getAll(studioId = studioId),
                search,
                filter
            ) { list, search, filter ->
                _state.update {
                    it.copy(
                        actors = if (search.isEmpty() && filter.dummy == null) {
                            list.forEach { Log.d(TAG, "getActors: ${list.map { it.films }}") }
                            list
                        } else {
                            list.filter { actor ->
                                actor.entity.name.contains(search, ignoreCase = true)
                                        || actor.entity.nickName?.contains(search, ignoreCase = true) ?: true
                                        || actor.entity.role?.contains(search, ignoreCase = true) ?: true
                            }
                        }
                    )
                }
            }.collect {}
        }
    }
}

data class ActorFilter(
    val dummy: Int? = null
)