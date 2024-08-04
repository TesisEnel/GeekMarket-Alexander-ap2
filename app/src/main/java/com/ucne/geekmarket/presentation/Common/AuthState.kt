package com.ucne.geekmarket.presentation.Common

sealed class AuthState{
    data class Authenticated(val email : String) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}