package com.ucne.alexandersuarez_ap2_p1.presentation.navigation


import kotlinx.serialization.Serializable


sealed class Screen {
    @Serializable
    data object ProductList : Screen()
    @Serializable
    data object CarritoList : Screen()
    @Serializable
    data class ProductDetail(val productoId: Int) : Screen()

}

