package com.ucne.alexandersuarez_ap2_p1.presentation.navigation


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
    data class Categoria(val categoria: String) : Screen()

    @Serializable
    data class ProductDetail(val productoId: Int) : Screen()

}

