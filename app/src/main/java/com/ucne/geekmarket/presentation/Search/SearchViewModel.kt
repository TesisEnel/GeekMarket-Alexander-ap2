package com.ucne.geekmarket.presentation.Search


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import com.ucne.geekmarket.data.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow((SearchUistate()))
    val uiState = _uiState.asStateFlow()



    fun search(query: String) {
        viewModelScope.launch {
            var producto by mutableStateOf(emptyList<ProductoEntity>())
            producto = productoRepository.searchProducto(query)
            _uiState.update {
                it.copy(
                    productos = producto
                )
            }

        }
    }

    fun getProductos() {
        viewModelScope.launch {
            productoRepository.getApiToDb()
        }
    }


}

data class SearchUistate(
    var productos: List<ProductoEntity> = emptyList(),
    var paramater: String? = "",
    val errorMessage: String? = null
)

