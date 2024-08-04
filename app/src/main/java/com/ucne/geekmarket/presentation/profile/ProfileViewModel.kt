package com.ucne.geekmarket.presentation.profile

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
class ProfileViewModel @Inject constructor(
     private val authRepository: AuthRepository,
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _uiState = MutableStateFlow((ProfileUistate()))
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        viewModelScope.launch {
            authRepository.checkAuthStatus().collect{
                _authState.value = it
            }
            if(_authState.value is AuthState.Authenticated){
                _uiState.update {
                    it.copy(
                        email = (_authState.value as AuthState.Authenticated).email
                    )
                }
            }
        }
    }

    fun signout(){
        viewModelScope.launch {
            authRepository.signout().collect{
                _authState.value = it
            }
        }
    }


}
data class ProfileUistate(
    val email: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)



