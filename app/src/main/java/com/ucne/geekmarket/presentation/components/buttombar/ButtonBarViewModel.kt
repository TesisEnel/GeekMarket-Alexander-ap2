package com.ucne.geekmarket.presentation.components.buttombar


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.repository.AuthRepository
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.PersonaRepository
import com.ucne.geekmarket.data.repository.WishListRepository
import com.ucne.geekmarket.presentation.Common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val authRepository: AuthRepository,
    private val personaRepository: PersonaRepository,
    private val wishRepository: WishListRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((ButtonBarUistate()))
    val uiState = _uiState.asStateFlow()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    init {
        checkAuthStatus()

    }

    val items = itemRepository.getItem()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.checkAuthStatus().collect {
                _authState.value = it
            }
        }
    }

//    fun getItemsQuantity() {
//        viewModelScope.launch {
//            val persona = personaRepository.getPersonaByEmail(
//                if (_authState.value is AuthState.Authenticated)
//                    (_authState.value as AuthState.Authenticated).email
//                else ""
//            )
//            val lastCarrito = carritoRepository.getLastCarritoByPersona(persona?.personaId ?: 0)
//            getItems(lastCarrito)
//
//        }
//    }
//
//    private fun getItems(lastCarrito: CarritoEntity?) {
//        viewModelScope.launch {
//            itemRepository.carritoItems(lastCarrito?.carritoId ?: 0)?.collect { result ->
//                _uiState.update {
//                    it.copy(
//                        itemsQuantity = result.size
//                    )
//                }
//            }
//        }
//    }

    fun getItemsQuantity() {
        viewModelScope.launch {
            val persona = personaRepository.getPersonaByEmail(
                if (_authState.value is AuthState.Authenticated)
                    (_authState.value as AuthState.Authenticated).email
                else ""
            )
            itemRepository.getItemsCountByPersona(persona?.personaId ?: 0).collect { quantity ->
                _uiState.update {
                    it.copy(
                        itemsQuantity = quantity
                    )
                }
            }
        }
    }

    fun getWishQuantity() {
        viewModelScope.launch {
            val persona = personaRepository.getPersonaByEmail(
                if (_authState.value is AuthState.Authenticated)
                    (_authState.value as AuthState.Authenticated).email
                else ""
            )
            wishRepository.productosByPersona(persona?.personaId ?: 0)?.collect{
                    productos ->
                _uiState.update {
                    it.copy(
                        wishQuantity = productos.size,
                    )
                }
            }

        }
    }

//    fun getWishQuantity() {
//        viewModelScope.launch {
//            val persona = personaRepository.getPersonaByEmail(
//                if (_authState.value is AuthState.Authenticated)
//                    (_authState.value as AuthState.Authenticated).email
//                else ""
//            )
//            wishRepository.getWishCountByPersona(persona?.personaId ?: 0).collect { quantity ->
//                _uiState.update {
//                    it.copy(
//                        wishQuantity = quantity
//                    )
//                }
//            }
//        }
//    }

}


data class ButtonBarUistate(
    val itemsQuantity: Int? = null,
    val wishQuantity: Int? = null,
    val errorMessage: String? = null
)

