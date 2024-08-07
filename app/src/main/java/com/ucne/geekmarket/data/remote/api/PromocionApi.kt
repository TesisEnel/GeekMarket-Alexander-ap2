package com.ucne.geekmarket.data.remote.api

import com.ucne.geekmarket.data.remote.dto.PromocionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PromocionApi {
    @GET("api/Promociones/{id}")
    suspend fun getPromocion(@Path("id") id: Int): PromocionDto
    @GET("api/Promociones")
    suspend fun getPromociones(): List<PromocionDto>
    @POST("api/Promociones")
    suspend fun savePromocion(@Body promocionDto: PromocionDto?): PromocionDto?
    @PUT("api/Promociones/{id}")
    suspend fun updatePromocion(@Path("id") id: Int, @Body productoDto: PromocionDto?): Response<PromocionDto>
    @DELETE("api/Promociones/{id}")
    suspend fun deletePromocion(@Path("id") id: Int): Response<Unit>
}