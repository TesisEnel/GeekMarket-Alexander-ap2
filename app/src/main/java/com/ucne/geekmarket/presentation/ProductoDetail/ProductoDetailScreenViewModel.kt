package com.ucne.geekmarket.presentation.ProductoDetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.data.repository.WishListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoDetailScreenViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val itemRepository: ItemRepository,
    private val wishRepository: WishListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((ProductoDetailUiState()))
    val uiState = _uiState.asStateFlow()

    val items = itemRepository.getItem()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onAddWish(productoId: Int?) {
        viewModelScope.launch {
            var inWishList = wishRepository.itemExit(productoId?: 0,1)
            if(!inWishList){
                onSaveWish(productoId?: 0)
                inWishList = true
            }
            else{
                onDeleteWish(productoId?: 0)
                inWishList = false
            }
            _uiState.update {
                it.copy(
                    inWishList = inWishList
                )
            }
        }
    }
    fun onSaveWish(productoId: Int) {
        viewModelScope.launch {
            wishRepository.saveWish(
                WishEntity(
                    productoId = productoId,
                    personaId = 1
                )
            )
        }
    }
    fun onDeleteWish(productoId: Int) {
        viewModelScope.launch {
            val wish = wishRepository.WishListByProducto(productoId)
            wishRepository.deleteWish(wish?: WishEntity())
        }
    }

    fun onSetProducto(productoId: Int) {
        viewModelScope.launch {
            val inWishList = wishRepository.itemExit(productoId,1)
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
                        inWishList = inWishList,
                        stock = producto?.stock,
                    )
                }
            }
        }
    }

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.AddItem(item)
        }
    }

}


data class ProductoDetailUiState(
    val productoId: Int? = null,
    val nombre: String= "",
    val precio: Double? = null,
    val descripcion: String? = null,
    val stock: Int? = 0,
    val categoria: String? = null,
    val imagen: String? = null,
    val item: ItemEntity? = null,
    val especificacion: String? = null,
    val isLoading: Boolean = false,
    var inWishList: Boolean = false,
    val errorMessage: String? = null
)


fun ProductoDetailUiState.toDTO() = ProductoDto(
    productoId = productoId?: 0,
    nombre = nombre,
    precio = precio?: 0.0,
    descripcion = descripcion?: "",
    categoria = categoria?: "",
    imagen = imagen?: "",
    stocks = stock?: 0,
    especificacion = especificacion?: "",
)