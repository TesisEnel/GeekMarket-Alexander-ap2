package com.ucne.geekmarket.presentation.Productos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.remote.dto.PromocionDto
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.data.repository.PromcionRepository
import com.ucne.geekmarket.data.repository.Resource
import com.ucne.geekmarket.data.repository.WishListRepository
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
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val itemRepository: ItemRepository,
    private val promocionRepository: PromcionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((ProductoUistate()))
    val uiState = _uiState.asStateFlow()

    val laptops =  productoRepository.getProductoByCategoria("Laptop")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val descktops = productoRepository.getProductoByCategoria("Desktop")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val laptopsGaming = productoRepository.getProductoByCategoria("Laptop-Gaming")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val items = itemRepository.getItem()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val promociones = promocionRepository.getPromocionesDb()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val accesorios = productoRepository.getProductoByCategoria("Accesorio")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onAddItem(item: ItemEntity)
    {
        viewModelScope.launch {
            itemRepository.AddItem(item)
        }
    }

    init {
        getProductos()
        getPromociones()
    }

    fun getProductos() {
        viewModelScope.launch {
            productoRepository.getApiToDb()
        }
    }

    fun getPromociones() {
        viewModelScope.launch {
            promocionRepository.getApiToDb()
            _uiState.update {
                it.copy(
                    promociones = promociones.value
                )
            }
        }
    }
}


data class ProductoUistate(
    val laptops: List<ProductoEntity> = emptyList(),
    val descktops: List<ProductoEntity> = emptyList(),
    val productos: List<ProductoDto> = emptyList(),
    val laptopsGaming: List<ProductoEntity> = emptyList(),
    val promociones: List<PromocionEntity> = emptyList(),
    val errorMessage: String? = null
)