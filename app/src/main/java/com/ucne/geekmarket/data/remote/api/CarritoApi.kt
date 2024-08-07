package com.ucne.geekmarket.data.remote.api

import com.ucne.geekmarket.data.remote.dto.CarritoDto
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarritoApi {
    @GET("api/Carritos")
    suspend fun getCarritos(): List<CarritoDto>
    @POST("api/Carritos")
    suspend fun saveCarrito(@Body carritoDto: CarritoDto?): CarritoDto?
//    @PUT("api/Carritos/{id}")
//    suspend fun updateProducto(@Path("id") id: Int, @Body productoDto: ProductoDto?): Response<ProductoDto>
    @DELETE("api/Carritos/{id}")
    suspend fun deleteCarrito(@Path("id") id: Int): Response<Unit>
}