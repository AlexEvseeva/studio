package ua.rikutou.studio.ui.section.details

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
import ua.rikutou.studio.data.datasource.section.SectionDataSource
import ua.rikutou.studio.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class SectionDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sectionDataSource: SectionDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(SectionDetails.State())
    val state = _state.asStateFlow()
        .onStart {
            loadSection()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SectionDetails.State()
        )
    private val _event = MutableSharedFlow<SectionDetails.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: SectionDetails.Action) = viewModelScope.launch {
        when(action) {
            is SectionDetails.Action.OnNavigate -> {
                _event.emit(SectionDetails.Event.OnNavigate(destination = action.destination))
            }

            SectionDetails.Action.OnDelete -> delete()
        }
    }

    private fun loadSection() = viewModelScope.launch {
        savedStateHandle.toRoute<Screen.Section.Details>().sectionId?.let { id ->
            sectionDataSource.getSectionById(sectionId = id).collect { section ->
                _state.update {
                    it.copy(section = section)
                }
            }
        }
    }

    private fun delete() = viewModelScope.launch {
        state.value.section?.sectionId?.let {
            sectionDataSource.deleteSectionById(sectionId = it)
        }
        _event.emit(SectionDetails.Event.OnNavigate())
    }
}