package com.ucne.geekmarket.data.remote.dto

data class CarritoDto(
    val carritoId: Int? = null,
    var pagado: Boolean = false,
    var total: Double= 0.0,
    val personaId: Int? = null,
    val items: List<ItemDto>? = null
)
