package com.ucne.geekmarket.presentation.Productos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
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
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository,
    private val itemRepository: ItemRepository
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
    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.AddItem(item)
        }
//        viewModelScope.launch {
//            val existe = uiState.value.items?.any { it.productoId == item.productoId }
//            val producto = productoRepository.getProducto(item.productoId ?: 0)
//            val monto = (item.cantidad?.toDouble() ?: 0.0) * (producto?.precio ?: 0.0)
//            if (existe == false) {
//                itemRepository.saveItem(
//                    ItemEntity(
//                        carritoId = uiState.value.carritoId,
//                        productoId = item.productoId,
//                        cantidad = item.cantidad,
//                        monto = monto
//                    )
//                )
//                _uiState.update {
//                    it.copy(items = items.value)
//                }
//                calcularTotal()
//            } else {
//                val itemsRepetido =
//                    uiState.value.items?.filter { it.productoId == item.productoId }?.firstOrNull()
//                itemRepository.saveItem(
//                    ItemEntity(
//                        itemId = itemsRepetido?.itemId,
//                        carritoId = uiState.value.carritoId,
//                        productoId = item.productoId,
//                        cantidad = item.cantidad?.plus((itemsRepetido?.cantidad ?: 0)),
//                        monto = (item.cantidad?.toDouble() ?: 0.0) * (producto?.precio ?: 0.0)
//                    )
//                )
//                _uiState.update {
//                    it.copy(items = items.value)
//                }
//                calcularTotal()
//            }
//        }

    }
//    fun getLastCarrito() {
//        viewModelScope.launch {
//            val lastCarrito = carritoRepository.getLastCarrito()
//            val productoList = productoRepository.getProductosItem(lastCarrito?.carritoId ?: 0)
//            val itemList = itemRepository.carritoItems(lastCarrito?.carritoId ?: 0)
//            val total = items.value.sumOf { it.monto ?: 0.0 }
//            if (lastCarrito?.pagado == false && (lastCarrito.carritoId ?: 0) > 0) {
//                _uiState.update {
//                    it.copy(
//                        carritoId = lastCarrito.carritoId,
//                        total = total,
//                        personaId = lastCarrito.personaId,
//                        pagado = lastCarrito.pagado,
//                        items = itemList,
//                        productos = productoList,
//                    )
//                }
//
//            }
//            calcularTotal()
//
//
//
//
//        }
//    }
//    fun calcularTotal() {
//        viewModelScope.launch {
//            val total = items.value.sumOf { (it.monto ?: 0.0) }
//            uiState.update {
//                it.copy(
//                    total = total
//                )
//            }
//            saveCarrito()
//        }
//    }

    fun onSetProducto(productoId: Int) {
        viewModelScope.launch {
            val producto = productoRepository.getProducto(productoId)
            producto.let {
                _uiState.update {
                    it.copy(
                        productoId = producto?.productoId,
                        nombre = producto?.nombre?: "",
                        precio = producto?.precio,
                        descripcion = producto?.descripcion,
                        especificacion = producto?.especificacion,
                        categoria = producto?.categoria,
                        imagen = producto?.imagen,
                        stock = producto?.stock,
                    )
                }
            }
        }
    }

    fun getProductos() {

        productoRepository.getProductos().onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                       isLoading = false,
                        productos = result.data?: emptyList()
                    )
                }
                is Resource.Error -> _uiState.update {
                    it.copy(
                       isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}

data class ProductoUistate(
    val productoId: Int? = null,
    val nombre: String= "",
    val precio: Double? = null,
    val descripcion: String? = null,
    val stock: Int? = 0,
    val categoria: String? = null,
    val imagen: String? = null,
    val especificacion: String? = null,
    val isLoading: Boolean = false,
    val producto: ProductoDto? = null,
    val laptops: List<ProductoEntity> = emptyList(),
    val descktops: List<ProductoEntity> = emptyList(),
    val productos: List<ProductoDto> = emptyList(),
    val laptopsGaming: List<ProductoEntity> = emptyList(),
    val errorMessage: String? = null
)


fun ProductoUistate.toDTO() = ProductoDto(
    productoId = productoId?: 0,
    nombre = nombre,
    precio = precio?: 0.0,
    descripcion = descripcion?: "",
    categoria = categoria?: "",
    imagen = imagen?: "",
    stocks = stock?: 0,
    especificacion = especificacion?: "",
)