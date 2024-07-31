package com.ucne.geekmarket.presentation.wish


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import com.ucne.geekmarket.data.repository.WishListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val wishListRepository: WishListRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(wishListUistate())
    val uiState = _uiState.asStateFlow()

    init {
        getWishList()
    }

    fun getWishList() {
        viewModelScope.launch {
            val productos = wishListRepository.productosByPersona(1)
            _uiState.update {
                it.copy(
                    productos = productos
                )
            }
        }
    }

    fun saveCarrito() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    carritoId = uiState.value.carritoId,
                    personaId = 1,
                )
            }
            wishListRepository.saveWish(uiState.value.toEntity())
        }
    }

    fun deleteWish(productoId: Int) {
        viewModelScope.launch {
            val wish = wishListRepository.WishListByProducto(productoId)
            wishListRepository.deleteWish(wish?: WishEntity())
            val itemList = wishListRepository.productosByPersona(1)
            _uiState.update {
                it.copy(
                    productos = itemList
                )
            }
        }
        saveCarrito()
    }
}

data class wishListUistate(
    val wishId: Int? = null,
    val carritoId: Int? = null,
    val personaId: Int? = null,
    val productos: List<ProductoEntity>? = null,
    val errorMessage: String? = null
)

fun wishListUistate.toEntity() = WishEntity(
    wishId = wishId ?: 0,
    productoId = carritoId ?: 0,
    personaId = personaId ?: 0
)