package ua.rikutou.studio.ui.studio.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.rikutou.studio.data.datasource.studio.StudioDataSource
import javax.inject.Inject

@HiltViewModel
class StudioEditViewModel
@Inject constructor(
    private val studioDataSource: StudioDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(
        StudioEdit.State(
            studioId = savedStateHandle.get("studioId")
        )
    )
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<StudioEdit.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: StudioEdit.Action) {

    }
}