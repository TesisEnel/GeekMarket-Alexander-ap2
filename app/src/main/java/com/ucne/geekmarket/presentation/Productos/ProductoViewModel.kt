package com.ucne.geekmarket.presentation.Productos


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.repository.AuthRepository
import com.ucne.geekmarket.data.repository.ItemRepository
import com.ucne.geekmarket.data.repository.PersonaRepository
import com.ucne.geekmarket.data.repository.ProductoRepository
import com.ucne.geekmarket.data.repository.PromcionRepository
import com.ucne.geekmarket.presentation.Common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val promocionRepository: PromcionRepository,
    private val authRepository: AuthRepository,
    private val personaRepository: PersonaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow((ProductoUistate()))
    val uiState = _uiState.asStateFlow()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun onAddItem(item: ItemEntity) {
        viewModelScope.launch {
//            itemRepository.AddItem(item)
        }
    }

    init {
        loadProductos()
        getPromociones()
        getProductos()
        loadPersonaToDb()
    }

    private fun getProductos() {
        viewModelScope.launch {
            productoRepository.getProductosDb().collectLatest {
                result ->
                _uiState.update {
                    it.copy(
                        laptops = result.filter { it.categoria == "Laptop" },
                        accesorios = result.filter { it.categoria == "Accesorio" },
                        laptopsGaming = result.filter { it.categoria == "Laptop-Gaming" },
                        desktops = result.filter { it.categoria == "Desktop" }
                    )
                }
            }
        }
    }

    private fun loadPersonaToDb(){
        viewModelScope.launch {
            personaRepository.getAllPersonas().collect{
                _uiState.update {
                    it.copy(
                        errorMessage = it.errorMessage
                    )
                }
            }
        }
    }

    private fun loadProductos(){
        viewModelScope.launch {
            productoRepository.loadToDb().collect{
                _uiState.update {
                    it.copy(
                       errorMessage = it.errorMessage
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
    var promociones: List<PromocionEntity>? = emptyList(),
    var accesorios: List<ProductoEntity> = emptyList(),
    var laptops: List<ProductoEntity> = emptyList(),
    var laptopsGaming: List<ProductoEntity> = emptyList(),
    var desktops: List<ProductoEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
