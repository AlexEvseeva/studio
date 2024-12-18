package ua.rikutou.studio.ui.studio.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.datasource.statistic.StatisticDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val studioDataSource: StudioDataSource,
    private val profileDataSource: ProfileDataSource,
    private val statisticDataSource: StatisticDataSource
): ViewModel() {
    private val _state = MutableStateFlow(Main.State())
    val state = _state.asStateFlow().onStart {
        profileDataSource.user?.studioId?.let { studioId ->
            loadStudio()
            getUserInfo()
            getStudio()
            loadStatistic(studioId = studioId)
        }
    }
    private val _event = MutableSharedFlow<Main.Event>()
    val event = _event.asSharedFlow()

    private fun getStudio() {
        viewModelScope.launch {
            profileDataSource.user?.studioId?.let {
                studioDataSource.getStudioById(studioId = it).collect { studio ->
                    _state.update {
                        it.copy(studio = studio)
                    }
                }
            }

        }
    }

    private fun getUserInfo() = viewModelScope.launch {
        profileDataSource.userFlow.collect {
            it?.studioId?.let {
                getStudio()
            }
        }
    }

    private fun loadStudio() {
        viewModelScope.launch(Dispatchers.IO) {
            profileDataSource.userFlow.mapLatest {
                it?.studioId
            }.collect {
                it?.let { studioId ->
                    studioDataSource.loadStudioById(studioId =studioId)
                }
            }
        }
    }

    fun onAction(action: Main.Action) {
        viewModelScope.launch {
            when(action) {
                Main.Action.OnEdit -> {
                    _event.emit(Main.Event.OnNavigate(destination = Screen.Studio.Edit(studioId = _state.value.studio?.studioId)))
                }

                is Main.Action.OnNavigate -> {
                    _event.emit(Main.Event.OnNavigate(destination = action.destination))
                }
            }
        }
    }

    private fun loadStatistic(studioId: Long) = viewModelScope.launch(Dispatchers.IO) {
        statisticDataSource.getStatistic(studioId = studioId).collect {
            when(it) {
                is DataSourceResponse.Error<*> -> {
                    _state.update {
                        it.copy(
                            inProgress = false
                        )
                    }
                    it.message?.let {
                        _event.emit(Main.Event.OnMessage(message = it))
                    }
                }
                DataSourceResponse.InProgress -> {
                    _state.update {
                        it.copy(inProgress = true)
                    }
                }
                is DataSourceResponse.Success -> {
                    _state.update { s ->
                        s.copy(
                            inProgress = false,
                            statistic = it.payload
                        )
                    }
                }
            }
        }
    }
}