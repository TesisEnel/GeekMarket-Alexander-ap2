package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Productos")
class ProductoEntity (
    @PrimaryKey
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val stock: Int,
    val categoria: String,
    val imagen: String,
)