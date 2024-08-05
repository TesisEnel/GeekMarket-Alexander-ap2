package com.ucne.geekmarket.presentation.wish


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import com.ucne.geekmarket.data.repository.AuthRepository
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.PersonaRepository
import com.ucne.geekmarket.data.repository.WishListRepository
import com.ucne.geekmarket.presentation.Common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val wishListRepository: WishListRepository,
    private val itemRepository: ItemRepository,
    private val authRepository: AuthRepository,
    private val personaRepository:PersonaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishListUistate())
    val uiState = _uiState.asStateFlow()

    private val _authState = MutableLiveData<AuthState>()
    val authState: MutableLiveData<AuthState> = _authState

    init {
        checkAuthStatus()
        getWishList()
    }
    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.checkAuthStatus().collect {
                _authState.value = it
            }
        }
    }

    private fun getWishList() {
        viewModelScope.launch {
            val persona = personaRepository.getPersonaByEmail(
                if (_authState.value is AuthState.Authenticated)
                    (_authState.value as AuthState.Authenticated).email
                else ""
            )
            wishListRepository.productosByPersona(persona?.personaId ?: 0)?.collect{
                productos ->
                _uiState.update {
                    it.copy(
                        productos = productos,
                    )
                }
            }

        }
    }

    private fun saveCarrito() {
            viewModelScope.launch {
                val  persona = personaRepository.getPersonaByEmail(
                    if(_authState.value is AuthState.Authenticated)
                    (_authState.value as AuthState.Authenticated).email
                    else ""
                )
                _uiState.update {
                    it.copy(
                        carritoId = uiState.value.carritoId,
                        personaId =persona?.personaId ?:0,
                    )
                }
                wishListRepository.saveWish(uiState.value.toEntity())
            }
    }

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.AddItem(item, _authState.value?: AuthState.Unauthenticated)
        }
    }

    fun deleteWish(productoId: Int) {
        viewModelScope.launch {
            val  persona = personaRepository.getPersonaByEmail(
                if(_authState.value is AuthState.Authenticated)
                    (_authState.value as AuthState.Authenticated).email
                else ""
            )
            val wish = wishListRepository.WishListByProducto(productoId, persona?.personaId ?: 0)
            wishListRepository.deleteWish(wish?: WishEntity())

        }
        saveCarrito()
    }
}

data class WishListUistate(
    val wishId: Int? = null,
    val carritoId: Int? = null,
    val personaId: Int? = null,
    val productos: List<ProductoEntity>? = null,
    val item: ItemEntity? = null,
    val errorMessage: String? = null
)

fun WishListUistate.toEntity() = WishEntity(
    wishId = wishId ?: 0,
    productoId = carritoId ?: 0,
    personaId = personaId ?: 0
)