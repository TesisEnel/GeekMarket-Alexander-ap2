package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ucne.geekmarket.data.remote.dto.ProductoDto

@Entity(tableName = "Carritos")
data class CarritoEntity(
    @PrimaryKey
    val carritoId: Int? = null,
    val pagado: Boolean = false,
    val Monto: Double= 0.0,
    var Items: List<Items> = emptyList(),
    val personaId: Int? = null
)
@Entity(tableName = "Items")
data class Items(
    @PrimaryKey
    val itemId: Int?= null,
    val producto: ProductoDto?= null,
    val cantidad: Int?= null,
)
