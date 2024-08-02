package com.ucne.geekmarket.presentation.Productos


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.data.repository.PromcionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val itemRepository: ItemRepository,
    private val promocionRepository: PromcionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((ProductoUistate()))
    val uiState = _uiState.asStateFlow()

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
            itemRepository.AddItem(item)
        }
    }

    init {
        getPromociones()
        getProductos()
        getLaptops()
        getAccesorios()
        getLaptopsGaming()
        getDesktops()
    }

    private fun getProductos() {
        viewModelScope.launch {
            productoRepository.getApiToDb()
        }
    }


    private fun getLaptops() {
        viewModelScope.launch {
            productoRepository.getProductoByCategoria("Laptop")
                .collectLatest { laptops ->
                    _uiState.update {
                        it.copy(
                            laptops = laptops
                        )
                    }
                }
        }
    }

    private fun getAccesorios() {
        viewModelScope.launch {
            productoRepository.getProductoByCategoria("Accesorio")
                .collectLatest { accesorios ->
                    _uiState.update {
                        it.copy(
                            accesorios = accesorios
                        )
                    }
                }
        }
    }

    private fun getLaptopsGaming() {
        viewModelScope.launch {
            productoRepository.getProductoByCategoria("Laptop-Gaming")
                .collectLatest { laptopsGaming ->
                    _uiState.update {
                        it.copy(
                            laptopsGaming = laptopsGaming
                        )
                    }
                }
        }
    }

    private fun getDesktops() {
        viewModelScope.launch {
            productoRepository.getProductoByCategoria("Desktop")
                .collectLatest { desktops ->
                    _uiState.update {
                        it.copy(
                            desktops = desktops
                        )
                    }
                }
        }
    }

    private fun getPromociones() {
        viewModelScope.launch {
            promocionRepository.getApiToDb()
            promocionRepository.getPromocionesDb().collectLatest { promociones ->
                _uiState.update {
                    it.copy(
                        promociones = promociones
                    )
                }
            }
        }
    }
}


data class ProductoUistate(
    var laptops: List<ProductoEntity> = emptyList(),
    var desktops: List<ProductoEntity> = emptyList(),
    var productos: List<ProductoDto> = emptyList(),
    var accesorios: List<ProductoEntity> = emptyList(),
    var laptopsGaming: List<ProductoEntity> = emptyList(),
    var promociones: List<PromocionEntity>? = emptyList(),
    val errorMessage: String? = null
)