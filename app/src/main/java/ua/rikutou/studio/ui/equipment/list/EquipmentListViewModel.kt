package ua.rikutou.studio.ui.equipment.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.equipment.EquipmentDataSource
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource
import ua.rikutou.studio.data.local.entity.EquipmentEntity
import ua.rikutou.studio.data.remote.equipment.EquipmentType
import javax.inject.Inject

@HiltViewModel
class EquipmentListViewModel @Inject constructor(
    private val equipmentDataSource: EquipmentDataSource,
    private val profileDataSource: ProfileDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(EquipmentList.State())
    val filter = MutableStateFlow(EquipmentFilter())
    val state = _state
        .onStart {
            profileDataSource.user?.studioId?.let {
                loadEquipments(studioId = it, search = "")
                getEquipment(studioId = it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    private val _event = MutableSharedFlow<EquipmentList.Event>()
    val event = _event.asSharedFlow()

    private val search = MutableStateFlow("")
    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                search,
                filter
            ) { search, filter ->
                if (search.length > 2 || filter.byType != null) {
                    profileDataSource.user?.studioId?.let { id ->
                        loadEquipments(
                            studioId = id,
                            search = search,
                            byType = filter.byType
                        )
                    }
                }
            }.collect()
        }
    }

    fun onAction(action: EquipmentList.Action) =
        viewModelScope.launch {
            when(action) {
                is EquipmentList.Action.OnNavigate -> {
                    _event.emit(EquipmentList.Event.OnNavigate(destionation = action.destionation))
                }
                EquipmentList.Action.OnCancel -> {
                    _state.update {
                        it.copy(
                            isSearchActive = false,
                            isSearchEnabled = true,
                        )
                    }
                    search.value = ""
                }

                EquipmentList.Action.OnSearch -> {
                    _state.update {
                        it.copy(
                            isSearchActive = true,
                            isSearchEnabled = false
                        )
                    }
                }
                is EquipmentList.Action.OnSearchChanged -> {
                    search.value = action.search
                }

                is EquipmentList.Action.OnAddToCart -> {
                    equipmentDataSource.addToCart(equipmentId = action.equipmentId)
                }

                is EquipmentList.Action.OnOrder -> {
                    filter.update {
                        it.copy(order = action.order)
                    }
                }
                is EquipmentList.Action.OnType -> {
                    filter.update {
                        it.copy(byType = action.type)
                    }
                }

                EquipmentList.Action.OnClearFilter -> {
                    _state.update {
                        it.copy(
                            isSearchActive = false,
                            isSearchEnabled = true,
                        )
                    }
                    search.value = ""
                    filter.update {
                        it.copy(byType = null, order = EquipmentOrder.DESC)
                    }
                }
            }
        }

    private fun loadEquipments(studioId: Long, search: String, byType: EquipmentType? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            equipmentDataSource.loadEquipment(studioId = studioId, search = search, type = byType)
        }
    }

    private fun getEquipment(studioId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                equipmentDataSource.getAllEquipment(studioId = studioId),
                equipmentDataSource.getAllSelected(),
                search,
                filter
            ) { list, selected, search, filter ->

                val equipment = if (search.isEmpty() && filter.byType == null) {
                    list
                } else {
                    list.filter { equipment ->
                        (equipment.name.contains(search,ignoreCase = true)
                                || equipment.comment.contains(search, ignoreCase = true))
                                && filter.byType?.let { equipment.type == it } ?: true
                    }
                }

                _state.update {
                    it.copy(
                        equipment = when(filter.order) {
                            EquipmentOrder.ASC -> equipment.sortedBy { it.rentPrice }
                            EquipmentOrder.DESC -> equipment.sortedByDescending { it.rentPrice }
                        }.map {
                            EquipmentHolder(equipment = it, isSelected = it.equipmentId in selected)
                        }
                    )
                }
            }.collect {}
        }
    }
}

data class EquipmentHolder(
    val equipment: EquipmentEntity,
    val isSelected: Boolean = false,
)

data class EquipmentFilter(
    val byType: EquipmentType? = null,
    val order: EquipmentOrder = EquipmentOrder.DESC
)

enum class EquipmentOrder {
    ASC, DESC
}