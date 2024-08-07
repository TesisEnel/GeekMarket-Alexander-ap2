package com.ucne.geekmarket.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.local.entities.PersonaEntity
import com.ucne.geekmarket.data.remote.api.PersonaApi
import com.ucne.geekmarket.data.remote.dto.PersonaDto
import com.ucne.geekmarket.data.repository.AuthRepository
import com.ucne.geekmarket.data.repository.PersonaRepository
import com.ucne.geekmarket.presentation.Common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val personaRepository: PersonaRepository,
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _uiState = MutableStateFlow((LoginUistate()))
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            authRepository.checkAuthStatus().collect {
                _authState.value = it
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            authRepository.login(
                _uiState.value.email ?: "",
                _uiState.value.password ?: ""
            ).collect {
                _authState.value = it
            }
        }
    }

    fun cambiarPersonaId() {
        if(_authState.value is AuthState.Authenticated){
            viewModelScope.launch {
                personaRepository.savePersona(
                    PersonaEntity(
                        personaId = _uiState.value.personaId ?: 0,
                        nombre = _uiState.value.nombre ?: "",
                        apellido = _uiState.value.apellido ?: "",
                        fechaNacimiento = _uiState.value.FechaNacimiento ?: "",
                        email = _uiState.value.email ?: ""
                    )
                )
                personaRepository.updatePersonaId(_uiState.value.personaId ?: 0)
            }
        }
    }

}

data class LoginUistate(
    var email: String? = null,
    var personaId: Int? = null,
    var nombre: String? = null,
    var apellido: String? = null,
    var FechaNacimiento: String? = null,
    var password: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)



