package com.ucne.geekmarket.presentation.components.buttombar


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ButtonBarViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow((ButtonBarUistate()))
    val uiState = _uiState.asStateFlow()

    val items = itemRepository.getItem()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    init {
//        getPromociones()
    }


    fun getPromociones() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    quantity = itemRepository.getItemsCount()
                )
            }
        }
    }

}


data class ButtonBarUistate(
    val quantity: Int? = null,
    val errorMessage: String? = null
)

