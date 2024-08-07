package com.ucne.geekmarket.data.remote.api

import com.ucne.geekmarket.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApi {
    @GET("api/Productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): ProductoDto
    @GET("api/Productos")
    suspend fun getProductos(): List<ProductoDto>
    @GET("api/Productos/Categorias/{categoria}")
    suspend fun getProductos(@Path("categoria") categoria: String): List<ProductoDto>
    @POST("api/Productos")
    suspend fun saveProducto(@Body ticketDto: ProductoDto?):ProductoDto?
    @PUT("api/Productos/{id}")
    suspend fun updateProducto(@Path("id") id: Int, @Body productoDto: ProductoDto?): Response<ProductoDto>
    @DELETE("api/Productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Unit>
}