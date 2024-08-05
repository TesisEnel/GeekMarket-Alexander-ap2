package com.ucne.geekmarket.presentation.Carritos


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.repository.AuthRepository
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.PersonaRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.presentation.Common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val itemRepository: ItemRepository,
    private val productoRepository: ProductoRepository,
    private val personaRepository: PersonaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(carritoUistate())
    val uiState = _uiState.asStateFlow()

    private val _authState = MutableLiveData<AuthState>()
    val authState: MutableLiveData<AuthState> = _authState

    init {
        checkAuthStatus()
        getLastCarritoByPersona()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.checkAuthStatus().collect {
                _authState.value = it
            }
        }
    }

    private fun getLastCarritoByPersona() {
        viewModelScope.launch {
            val persona = personaRepository.getPersonaByEmail(
                if (_authState.value is AuthState.Authenticated)
                    (_authState.value as AuthState.Authenticated).email
                else ""
            )
            val lastCarrito = carritoRepository.getLastCarritoByPersona(persona?.personaId ?: 0)
            getProductos(lastCarrito)
            getItems(lastCarrito)
            if (lastCarrito?.pagado == false) {
                _uiState.update {
                    it.copy(
                        carritoId = lastCarrito.carritoId,
                        total = lastCarrito.total,
                        personaId = lastCarrito.personaId,
                        pagado = lastCarrito.pagado,
                    )
                }
                getPersonaByEmail()
            }
        }
    }

    private fun getPersonaByEmail() {
        if (_authState.value is AuthState.Unauthenticated) return

        val email = (_authState.value as AuthState.Authenticated).email
        viewModelScope.launch {
            val persona = personaRepository.getPersonaByEmail(email)
            _uiState.update {
                it.copy(
                    personaId = persona?.personaId
                )
            }
        }
    }


    private fun getItems(lastCarrito: CarritoEntity?) {
        viewModelScope.launch {
            itemRepository.carritoItems(lastCarrito?.carritoId ?: 0)?.collect { result ->
                _uiState.update {
                    it.copy(
                        items = result
                    )
                }
            }
        }
    }

    private fun getProductos(lastCarrito: CarritoEntity?) {
        viewModelScope.launch {
            productoRepository.getProductoByCarritoId(lastCarrito?.carritoId ?: 0)
                .collect { result ->
                    _uiState.update {
                        it.copy(
                            productos = result
                        )
                    }
                }
        }
    }

    private fun saveCarrito() {
        viewModelScope.launch {
            carritoRepository.saveCarrito(_uiState.value.toEntity())
            if (_uiState.value.pagado == true) {
                cleanCarrito()
            }
        }
    }

    fun realizarPago() {
        if (_uiState.value.items?.isNotEmpty() == true) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        pagado = true
                    )
                }
            }
            saveCarrito()
        }
    }

    fun cleanCarrito() {
        viewModelScope.launch {
            _uiState.value = carritoUistate()
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.deleteItem(item)
            _uiState.update {
                it.copy(
                    total = itemRepository.getMontoTotal(item.carritoId ?: 0)
                )
            }
        }
    }

}

data class carritoUistate(
    var carritoId: Int? = null,
    var productos: List<ProductoEntity>? = null,
    var items: List<ItemEntity>? = null,
    var total: Double? = null,
    var pagado: Boolean? = null,
    var personaId: Int? = null,
    var email: String? = null,
    var errorMessage: String? = null
)


fun carritoUistate.toEntity() = CarritoEntity(
    carritoId = carritoId,
    total = total ?: 0.0,
    personaId = personaId ?: 0,
    pagado = pagado ?: false
)