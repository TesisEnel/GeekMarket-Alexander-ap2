package com.ucne.geekmarket.data.remote.api

import com.ucne.geekmarket.data.remote.dto.PersonaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PersonaApi {
    @GET("api/Personas/{id}")
    suspend fun getPersona(@Path("id") id: Int): PersonaDto
    @GET("api/Personas")
    suspend fun getPersonas(): List<PersonaDto>
    @GET("api/Personas/Email/{email}")
    suspend fun getPersonaByEmail(@Path("email") email: String): PersonaDto?
    @POST("api/Personas")
    suspend fun savePersonas(@Body personaDto: PersonaDto?): PersonaDto?
    @PUT("api/Personas/{id}")
    suspend fun updatePersonas(@Path("id") id: Int, @Body productoDto: PersonaDto?): Response<PersonaDto>
    @DELETE("api/Personas/{id}")
    suspend fun deletePersonas(@Path("id") id: Int): Response<Unit>
}