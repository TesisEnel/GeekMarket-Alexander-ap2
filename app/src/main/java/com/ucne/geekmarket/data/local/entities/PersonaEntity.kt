package com.ucne.geekmarket.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Personas")
data class PersonaEntity(
    @PrimaryKey
    val personaId: Int?=null,
    val nombre: String?=null,
    val apellido: String?=null,
    val edad: Int?=null,
    val correo: String?=null
)
