package ua.rikutou.studio.ui.document.locations_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import javax.inject.Inject

@HiltViewModel
class LocationsReportViewModel @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val profileDataSource: ProfileDataSource,
) : ViewModel() {
    val _state = MutableStateFlow(LocationsReport.State())
    val state = _state
        .onStart {
            profileDataSource.user?.studioId?.let { studioId ->
                loadLocationsReport(studioId = studioId)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    val _event = MutableSharedFlow<LocationsReport.Event>()
    val event = _event.asSharedFlow()

    var loadLocationJob: Job? = null

    fun onAction(action: LocationsReport.Action) = viewModelScope.launch {
        when(action) {
            LocationsReport.Action.OnCreateDocument -> {
                _event.emit(LocationsReport.Event.OnNavigate(destination = null))
            }
            is LocationsReport.Action.OnNavigate -> {
                _event.emit(LocationsReport.Event.OnNavigate(destination = action.destination))
            }
        }
    }

    private fun loadLocationsReport(studioId: Long) {
        loadLocationJob?.cancel()
        loadLocationJob = viewModelScope.launch {
            locationDataSource.getLocationsReport(studioId = studioId).collect { report ->
                _state.update {
                    it.copy(
                        inProgress = false,
                        report = report
                    )
                }
            }
        }
    }

}