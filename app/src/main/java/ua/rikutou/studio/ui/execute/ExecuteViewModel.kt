package ua.rikutou.studio.ui.execute

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.DataSourceResponse
import ua.rikutou.studio.data.datasource.execute.ExecuteDataSource
import javax.inject.Inject

@HiltViewModel
class ExecuteViewModel @Inject constructor(
    private val executeDataSource: ExecuteDataSource
) : ViewModel() {
    private val TAG by lazy { ExecuteViewModel::class.simpleName }
    private val _state = MutableStateFlow(Execute.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Execute.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: Execute.Action) {
        when(action) {
            Execute.Action.OnQuery ->  {
                executeQuery(state.value.query)
            }
            is Execute.Action.OnQueryStringChanged -> {
                _state.update {
                    it.copy(
                        query = action.query
                    )
                }
            }

            Execute.Action.OnClear -> {
                _state.update {
                    it.copy(
                        query = "SELECT",
                        columnNames = emptyList(),
                        queryResult = emptyList()
                    )
                }
            }
        }
    }

    private fun executeQuery(query: String) {
        if(query.isNotEmpty()) {
            _state.update {
                it.copy(
                    columnNames = emptyList(),
                    queryResult = emptyList()
                )
            }
            viewModelScope.launch {
                executeDataSource.executeQuery(query).collect { result ->
                    when(result) {
                        is DataSourceResponse.Error<*> -> {
                            _state.update {
                                it.copy(inProgress = false)
                            }
                        }
                        DataSourceResponse.InProgress -> {
                            _state.update {
                                it.copy(inProgress = true)
                            }
                        }
                        is DataSourceResponse.Success -> {
                            _state.update {
                                it.copy(
                                    inProgress = false,
                                    columnNames = result.payload?.columns ?: emptyList(),
                                    queryResult = result.payload?.queryResult ?: emptyList()
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}