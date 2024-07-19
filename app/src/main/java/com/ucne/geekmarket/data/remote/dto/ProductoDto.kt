package com.ucne.geekmarket.data.remote.dto

data class ProductoDto(
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val stocks: Int?,
    val categoria: String,
    val imagen: String,
    val especificacion: String?
)
