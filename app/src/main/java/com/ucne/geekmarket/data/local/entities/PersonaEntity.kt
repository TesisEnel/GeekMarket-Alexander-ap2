package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Personas")
data class PersonaEntity(
    @PrimaryKey
    val personaId: Int,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val Correo: String
)
