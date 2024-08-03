package com.ucne.geekmarket.presentation.navigation
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object ProductList : Screen()
    @Serializable
    data object CarritoList : Screen()
    @Serializable
    data object WishList : Screen()
    @Serializable
    data object Profile : Screen()
    @Serializable
    data object Search : Screen()
    @Serializable
    data class Categoria(val categoria: String) : Screen()
    @Serializable
    data class ProductDetail(val productoId: Int) : Screen()
}

