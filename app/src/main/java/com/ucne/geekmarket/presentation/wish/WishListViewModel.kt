package com.ucne.geekmarket.presentation.wish


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import com.ucne.geekmarket.data.repository.CarritoRepository
import com.ucne.geekmarket.data.repository.ItemRepository
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
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishListUistate())
    val uiState = _uiState.asStateFlow()

    init {
        getWishList()
    }

    private fun getWishList() {
        viewModelScope.launch {
            val productos = wishListRepository.productosByPersona(1)
            _uiState.update {
                it.copy(
                    productos = productos
                )
            }
        }
    }

    private fun saveCarrito() {
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

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.AddItem(item)
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