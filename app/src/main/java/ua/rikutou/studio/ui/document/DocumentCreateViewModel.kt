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
import ua.rikutou.studio.data.datasource.location.LocationDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import javax.inject.Inject

@HiltViewModel
class DocumentCreateViewModel @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val profileDataSource: ProfileDataSource,
) : ViewModel() {
    private val TAG by lazy { DocumentCreateViewModel::class.simpleName }
    private val _state = MutableStateFlow(DocumentCreate.State())
    val state = _state.onStart {
        getSelectedLocations()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _state.value
    )

    private val _event = MutableSharedFlow<DocumentCreate.Event>()
    val event = _event.asSharedFlow()

    fun onAction(actin: DocumentCreate.Action) {

    }

    private fun getSelectedLocations() = viewModelScope.launch {
        profileDataSource.user?.studioId?.let { studioId ->
            combine(
                locationDataSource.getLocationsSelection(),
                locationDataSource.getLocationsByStudioId(studioId = studioId)
            ) { selected, list ->
                val filteredLocations = list.filter { it.location.locationId in selected }
                Log.d(TAG, "getSelectedLocations: ${selected.size}, ${list.size}: $filteredLocations")
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
    }
}