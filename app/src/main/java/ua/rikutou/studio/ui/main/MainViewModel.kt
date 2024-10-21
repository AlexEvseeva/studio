package ua.rikutou.studio.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(Main.State())
    val state = _state.asStateFlow()
    private val _event = MutableSharedFlow<Main.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: Main.Acton) {
//        when(action) {
//
//        }
    }
}