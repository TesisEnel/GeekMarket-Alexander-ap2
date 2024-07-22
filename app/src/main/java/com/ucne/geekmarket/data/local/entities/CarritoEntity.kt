package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Carritos")
data class CarritoEntity(
    @PrimaryKey
    val carritoId: Int? = null,
    val pagado: Boolean = false,
    val total: Double= 0.0,
    val personaId: Int? = null
)

