package ua.rikutou.studio.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import ua.rikutou.studio.data.datasource.user.UserDataSource
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val studioDataSource: StudioDataSource,
    private val userDataSource: UserDataSource
): ViewModel() {
    private val _state = MutableStateFlow(Main.State())
    val state = _state.asStateFlow().onStart {
        loadStudio()
        getStudio()
    }
    private val _event = MutableSharedFlow<Main.Event>()
    val event = _event.asSharedFlow()

    private fun getStudio() {
        viewModelScope.launch(Dispatchers.IO) {
            userDataSource.userFlow.mapLatest {
                it?.studioId
            }.collect {
                it?.let { studioId ->
                    studioDataSource.getStudioById(studioId = studioId).collect { studio ->
                        _state.update {
                            it.copy(studio = studio)
                        }
                    }
                }
            }
        }
    }

    private fun loadStudio() {
        viewModelScope.launch(Dispatchers.IO) {
            userDataSource.userFlow.mapLatest {
                it?.studioId
            }.collect {
                it?.let { studioId ->
                    studioDataSource.loadStudioById(studioId =studioId)
                }
            }
        }
    }

    fun onAction(action: Main.Acton) {
//        when(action) {
//
//        }
    }
}