package com.ucne.geekmarket.data.remote.dto

data class PersonaDto(
    val personaId: Int?=null,
    val nombre: String?=null,
    val apellido: String?=null,
    val fechaNacimiento: String? = null,
    val email: String?=null
)
