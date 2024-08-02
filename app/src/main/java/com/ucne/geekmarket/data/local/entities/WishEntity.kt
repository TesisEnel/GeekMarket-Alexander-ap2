package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Wishes")
data class WishEntity(
    @PrimaryKey
    val wishId: Int?= null,
    val productoId: Int?= null,
    val personaId: Int?= null
)
