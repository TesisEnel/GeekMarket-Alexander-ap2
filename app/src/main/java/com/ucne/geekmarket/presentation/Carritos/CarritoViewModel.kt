package com.ucne.geekmarket.presentation.Carritos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.Items
import com.ucne.geekmarket.data.repository.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository
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

    fun getCarritoById(id: Int) {
        viewModelScope.launch {
            val carrito = carritoRepository.getCarritos(id)
            if (carrito != null) {
                uiState.update {
                    it.copy(
                        carritoId = carrito.carritoId,
                        items = carrito.Items,
                        Monto = carrito.Monto,
                        personaId = carrito.personaId,
                    )
                }
            }
        }

    }

//    fun pagarTodosLosCarritos(){
//        viewModelScope.launch {
//            carritos.value.forEach { c->
//                    c.update {
//                        it.copy(
//                            pagado = true
//                        )
//                    }
//                }
//            }
//    }

    fun onAddItem(item: Items) {
        val items = uiState.value.items?.toMutableList()
        items?.add(item)
        val itemsAdded = items?.toList()
        uiState.update {
            it.copy(items = itemsAdded)
        }
        saveCarrito()
    }

    fun getLastCarrito(){

        viewModelScope.launch {
        val lastCarrito = carritoRepository.getLastCarrito()
        if(lastCarrito?.pagado == false){
            uiState.update {
                it.copy(
                    carritoId = lastCarrito.carritoId,
                    items = lastCarrito.Items,
                    Monto = lastCarrito.Monto,
                    personaId = lastCarrito.personaId,
                    pagado = lastCarrito.pagado,
                )
            }

        }



        }
    }


    fun saveCarrito() {
        viewModelScope.launch {
//            if(validar()){
            val monto = uiState.value.items?.sumOf { (it.cantidad?: 0) * (it.producto?.precio?: 0.0) }
            uiState.update {
                it.copy(
                    carritoId = uiState.value.carritoId,
                    personaId = 1,
                    Monto = monto,
                    pagado = false,
                )
            }
            carritoRepository.saveCarrito(uiState.value.toEntity())
//                uiState.value = carritoUistate()
//            }
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
    fun deleteItem(productoId: Int){
        val items  =uiState.value.items?.toMutableList()
            items?.removeIf{it.producto?.productoId == productoId}
        uiState.update {
            it.copy(items = items)
        }
       saveCarrito()
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
    var items: List<Items>? = emptyList(),
    var Monto: Double? = null,
    val pagado: Boolean? = null,
    val personaId: Int? = null,
    val errorMessage: String? = null
)


fun carritoUistate.toEntity() = CarritoEntity(
    carritoId = carritoId,
    Items = items?: emptyList(),
    Monto = Monto ?: 0.0,
    personaId = personaId ?: 0,
    pagado = pagado ?: false
)