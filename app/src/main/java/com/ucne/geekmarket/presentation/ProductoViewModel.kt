package com.ucne.geekmarket.presentation


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
                        categoria = producto.categoria,
                        imagen = producto.imagen,
                        stock = producto.stock,
                    )
                }
            }
        }
    }

    fun onDescripcionChanged(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }


    fun onPrecioChanged(precio: String) {
        val letras = Regex("[a-zA-Z ]+")
        val numeros = precio.replace(letras, "").toDoubleOrNull()
        _uiState.update {
            it.copy(precio = numeros)
        }
    }

    fun setProducto() {
        viewModelScope.launch {
            val producto = productoRepository.getProducto(uiState.value.productoId ?: 0)
            producto?.let {
                _uiState.update {
                    it.copy(
                        productoId = producto.productoId,
                        nombre = producto.nombre,
                        precio = producto.precio,
                        descripcion = producto.descripcion,
                        stock = producto.stock,
                        imagen = producto.imagen,
                        categoria = producto.categoria,
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


    fun saveProducto(): Boolean {
        viewModelScope.launch {
            if (uiState.value.productoId == null || uiState.value.productoId == 0) {
                productoRepository.saveProducto(uiState.value.toDTO())
                _uiState.value = ProductoUistate()
            } else {
                productoRepository.updateProducto(uiState.value.toDTO())
                _uiState.value = ProductoUistate()
            }
        }
        return true
    }


    fun newProducto() {
        viewModelScope.launch {
            _uiState.value = ProductoUistate()
        }
    }

    fun deleteProducto() {
        viewModelScope.launch {
            productoRepository.deleteProducto(uiState.value.toDTO())
        }
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
    stock = stock?: 0,
    especificacion = especificacion?: "",
)