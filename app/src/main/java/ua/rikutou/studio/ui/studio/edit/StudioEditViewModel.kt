package ua.rikutou.studio.ui.studio.edit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import ua.rikutou.studio.data.local.entity.StudioEntity
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class StudioEditViewModel
@Inject constructor(
    private val studioDataSource: StudioDataSource,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val TAG by lazy { StudioEditViewModel::class.simpleName }
    private val _state = MutableStateFlow(StudioEdit.State())

    val state = _state.asStateFlow()
        .onStart {
            loadStudio()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = StudioEdit.State()
        )
    private val _event = MutableSharedFlow<StudioEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: StudioEdit.Action) {
        viewModelScope.launch { 
            when(action) {
                is StudioEdit.Action.OnFieldchanged -> {
                    action.name?.let { 
                        _state.update { 
                            it.copy(name = action.name)
                        }
                    }
                    action.address?.let {
                        _state.update {
                            it.copy(address = action.address)
                        }
                    }
                    action.postIndex?.let {
                        _state.update {
                            it.copy(postIndex = action.postIndex)
                        }
                    }
                    action.site?.let {
                        _state.update {
                            it.copy(site = action.site)
                        }
                    }
                    action.youtube?.let {
                        _state.update {
                            it.copy(youtube = action.youtube)
                        }
                    }
                    action.facebook?.let {
                        _state.update {
                            it.copy(facebook = action.facebook)
                        }
                    }
                }

                StudioEdit.Action.OnSave -> {
                    saveStudio()
                }
            }
        }
    }

    private fun loadStudio() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Studio.Edit>().studioId?.let { id ->
            studioDataSource.getStudioById(studioId = id).collect { studio ->
                _state.update {
                    it.copy(
                        studioId = studio?.studioId,
                        name = studio?.name,
                        address = studio?.address,
                        postIndex = studio?.postIndex,
                        site = studio?.site,
                        youtube = studio?.youtube,
                        facebook = studio?.facebook
                    )
                }
            }
        }
    }

    private fun saveStudio() = viewModelScope.launch {
        if(state.value.name?.isEmpty() == true
            && state.value.address?.isEmpty() == true
            && state.value.postIndex?.isEmpty() == true
            && state.value.site?.isEmpty() == true
            && state.value.youtube?.isEmpty() == true
            && state.value.facebook?.isEmpty() == true
            ) {
            return@launch
        } else {
            studioDataSource.createUpdateStudio(
                studio = StudioEntity(
                    studioId = state.value.studioId ?: -1L,
                    name = state.value.name ?: "",
                    address = state.value.address ?: "",
                    postIndex = state.value.postIndex ?: "",
                    site = state.value.site ?: "",
                    youtube = state.value.youtube ?: "",
                    facebook = state.value.facebook ?: ""
                )
            ).collect {
                when(it) {
                    is DataSourceResponse.Error<*> -> {
                        _state.update {
                            it.copy(inProgress = false)
                        }
                    }
                    DataSourceResponse.InProgress -> _state.update {
                        it.copy(inProgress = true)
                    }
                    is DataSourceResponse.Success -> {
                        _state.update {
                            it.copy(inProgress = false)
                        }
                        _event.emit(StudioEdit.Event.OnBack)
                    }
                }
            }
        }
    }
}

