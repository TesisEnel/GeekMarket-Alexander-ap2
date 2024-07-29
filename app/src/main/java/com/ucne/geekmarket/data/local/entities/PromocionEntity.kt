package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity( tableName = "Promociones")
data class PromocionEntity(
    @PrimaryKey
    val promocionId: Int,
    val productoId: Int,
    val imagen: String,
)
