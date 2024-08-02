package com.ucne.geekmarket.presentation.categoria


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.remote.dto.PromocionDto
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.data.repository.PromcionRepository
import com.ucne.geekmarket.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((CategoriaUistate()))
    val uiState = _uiState.asStateFlow()

    fun getByCategoria(categoria: String){
        viewModelScope.launch {
            val productos = productoRepository.getSuspendCategoria(categoria)
            _uiState.update {
                it.copy(
                    productos = productos,
                    categoria = categoria
                )
            }
        }
    }

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.AddItem(item)
        }
    }

}

data class CategoriaUistate(
    val productos: List<ProductoEntity>? = emptyList(),
    var categoria: String? = null,
    val errorMessage: String? = null
)