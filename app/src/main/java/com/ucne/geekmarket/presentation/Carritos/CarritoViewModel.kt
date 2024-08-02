package com.ucne.geekmarket.presentation.Carritos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.presentation.Productos.ProductoUistate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val itemRepository: ItemRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(carritoUistate())
    val uiState = _uiState.asStateFlow()

    val carritos = carritoRepository.getCarritos()
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
    val productos = productoRepository.getProductosDb()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        getLastCarrito()
    }

    private fun getLastCarrito() {
        viewModelScope.launch {
            val lastCarrito = carritoRepository.getLastCarrito()
            val productoList = productoRepository.getProductosItem(lastCarrito?.carritoId ?: 0)
            val itemList = itemRepository.carritoItems(lastCarrito?.carritoId ?: 0)
            if (lastCarrito?.pagado == false && (lastCarrito.carritoId ?: 0) > 0) {
                _uiState.update {
                    it.copy(
                        carritoId = lastCarrito.carritoId,
                        total = lastCarrito.total,
                        personaId = lastCarrito.personaId,
                        pagado = lastCarrito.pagado,
                        items = itemList,
                        productos = productoList,
                    )
                }
            }
        }
    }



    private fun saveCarrito() {
        viewModelScope.launch {
            val lastCarrito = carritoRepository.getLastCarrito()
            val itemList = itemRepository.carritoItems(lastCarrito?.carritoId ?: 0)
            val total = itemList?.sumOf { (it.monto ?: 0.0) }
            _uiState.update {
                it.copy(
                    carritoId = uiState.value.carritoId,
                    personaId = 1,
                    total = total,
                )
            }
            carritoRepository.saveCarrito(uiState.value.toEntity())
        }
    }

    fun realizarPago(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    pagado = true
                )
            }
        }
        saveCarrito()
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.deleteItem(item)
            val itemList = itemRepository.carritoItems(item.carritoId ?: 0)
            _uiState.update {
                it.copy(
                    items = itemList
                )
            }
        }
        saveCarrito()
    }
}

data class carritoUistate(
    val carritoId: Int? = null,
    val productos: List<ProductoEntity>? = null,
    val items: List<ItemEntity>? = null,
    var total: Double? = null,
    val pagado: Boolean? = null,
    val personaId: Int? = null,
    val errorMessage: String? = null
)


fun carritoUistate.toEntity() = CarritoEntity(
    carritoId = carritoId,
    total = total ?: 0.0,
    personaId = personaId ?: 0,
    pagado = pagado ?: false
)