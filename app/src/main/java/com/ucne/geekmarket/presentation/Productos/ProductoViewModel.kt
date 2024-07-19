package com.ucne.geekmarket.presentation.Productos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((ProductoUistate()))
    val uiState = _uiState.asStateFlow()

    fun onSetProducto(productoId: Int) {
        viewModelScope.launch {
            val producto = productoRepository.getProducto(productoId)
            producto?.let {
                _uiState.update {
                    it.copy(
                        productoId = producto.productoId,
                        nombre = producto.nombre,
                        precio = producto.precio,
                        descripcion = producto.descripcion,
                        especificacion = producto.especificacion,
                        categoria = producto.categoria,
                        imagen = producto.imagen,
                        stock = producto.stocks,
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
                        laptops = result.data?: emptyList()
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
    fun getProductosByCategoria() {

        productoRepository.getProductos("Laptop").onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                       isLoading = false,
                        laptops = result.data?: emptyList()
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

        productoRepository.getProductos("Desktop").onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                       isLoading = false,
                        descktops = result.data?: emptyList()
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

        productoRepository.getProductos("Laptop-Gaming").onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> _uiState.update {
                    it.copy(
                       isLoading = false,
                        laptopsGaming = result.data?: emptyList()
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
    val laptops: List<ProductoDto> = emptyList(),
    val descktops: List<ProductoDto> = emptyList(),
    val laptopsGaming: List<ProductoDto> = emptyList(),
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