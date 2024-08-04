package com.ucne.geekmarket.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.geekmarket.data.repository.AuthRepository
import com.ucne.geekmarket.presentation.Common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
     private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _uiState = MutableStateFlow((LoginUistate()))
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun onEmailChanged(email: String){
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }
    fun onPasswordChanged(password: String){
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    private fun checkAuthStatus(){
        viewModelScope.launch {
            authRepository.checkAuthStatus().collect{
                _authState.value = it
            }
        }
    }

    fun signup(){
        viewModelScope.launch {
            authRepository.signup(
                _uiState.value.email ?: "",
                _uiState.value.password ?: ""
            ).collect{
                _authState.value = it
            }
        }
    }

}

data class LoginUistate(
    var email: String? = null,
    var personaId: Int?=null,
    var nombre: String?=null,
    var apellido: String?=null,
    var edad: Int?=null,
    var password: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

