package com.ucne.geekmarket.presentation.Carritos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    private var carritoId: Int = 0
    var uiState = MutableStateFlow(carritoUistate())
        private set

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


    fun getCarritoById(id: Int) {
        viewModelScope.launch {
            val carrito = carritoRepository.getCarritos(id)
            if (carrito != null) {
                uiState.update {
                    it.copy(
                        carritoId = carrito.carritoId,
                        total = carrito.total,
                        personaId = carrito.personaId,
                        pagado = carrito.pagado,
                    )
                }
            }
        }

    }

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            val existe = uiState.value.items?.any { it.productoId == item.productoId }
            val producto = productoRepository.getProducto(item.productoId ?: 0)
            val monto = (item.cantidad?.toDouble() ?: 0.0) * (producto?.precio ?: 0.0)
            if (existe == false) {
                itemRepository.saveItem(
                    ItemEntity(
                        carritoId = uiState.value.carritoId,
                        productoId = item.productoId,
                        cantidad = item.cantidad,
                        monto = monto
                    )
                )
                uiState.update {
                    it.copy(items = items.value)
                }
                calcularTotal()
            } else {
                val itemsRepetido =
                    uiState.value.items?.filter { it.productoId == item.productoId }?.firstOrNull()
                itemRepository.saveItem(
                    ItemEntity(
                        itemId = itemsRepetido?.itemId,
                        carritoId = uiState.value.carritoId,
                        productoId = item.productoId,
                        cantidad = item.cantidad?.plus((itemsRepetido?.cantidad ?: 0)),
                        monto = (item.cantidad?.toDouble() ?: 0.0) * (producto?.precio ?: 0.0)
                    )
                )
                uiState.update {
                    it.copy(items = items.value)
                }
                calcularTotal()
            }
        }

    }

    fun getLastCarrito() {
        viewModelScope.launch {
            val lastCarrito = carritoRepository.getLastCarrito()
            val productoList = productoRepository.getProductosItem(lastCarrito?.carritoId ?: 0)
            val itemList = itemRepository.carritoItems(lastCarrito?.carritoId ?: 0)
            val total = items.value.sumOf { it.monto ?: 0.0 }
            if (lastCarrito?.pagado == false && (lastCarrito.carritoId ?: 0) > 0) {
                uiState.update {
                    it.copy(
                        carritoId = lastCarrito.carritoId,
                        total = total,
                        personaId = lastCarrito.personaId,
                        pagado = lastCarrito.pagado,
                        items = itemList,
                        productos = productoList,
                    )
                }

            }
//            calcularTotal()




        }
    }

    //Esta mal TODO
    fun calcularTotal() {
        viewModelScope.launch {
            val total = items.value.sumOf { (it.monto ?: 0.0) }
            uiState.update {
                it.copy(
                    total = total
                )
            }
            saveCarrito()
        }
    }

    fun saveCarrito() {
        viewModelScope.launch {
            val productoList = productoRepository.getProductosItem(uiState.value.carritoId ?: 0)
            val total = items.value.sumOf { (it.monto ?: 0.0) }
            uiState.update {
                it.copy(
                    carritoId = uiState.value.carritoId,
                    personaId = 1,
                    total = total,
                    pagado = false,
                )
            }
            carritoRepository.saveCarrito(uiState.value.toEntity())
//                uiState.value = carritoUistate()
//            }
        }
    }

    fun saveItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.saveItem(item)
        }
    }

    fun newServicio() {
        viewModelScope.launch {
            uiState.value = carritoUistate()
        }
    }

    fun deleteServicio() {
        viewModelScope.launch {
            carritoRepository.deleteCarrito(uiState.value.toEntity())
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.deleteItem(item)
        }

//        saveCarrito()
    }

//    private fun validar(): Boolean {
//        val descripcionVacio = (uiState.value.descripcion.isEmpty())
//        val precioNoIntroducido = ((uiState.value.precio ?: 0.0) <= 0.0)
////        val descripcionRepetido = repository.descripcionExiste(uiState.value.descripcion, uiState.value.servicoId?: 0)
//
//        val nombreError = when{
//            descripcionVacio -> "El nombre no puede estar vacio"
////            descripcionRepetido -> "El nombre ${uiState.value.descripcion} ya existe"
//            else -> null
//        }
//
//        val sueldoError = when{
//            precioNoIntroducido -> "Debe de ingresar un sueldo"
//            else -> null
//        }
//
//        uiState.update {
//            it.copy( descripcionError = nombreError, precioError = sueldoError)
//        }
//
//        return !descripcionVacio && !precioNoIntroducido
////                && !descripcionRepetido
//    }
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