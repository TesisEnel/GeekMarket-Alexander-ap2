package com.ucne.geekmarket.data.repository

import android.util.Log
import com.ucne.geekmarket.data.remote.ProductoApi
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val productoApi: ProductoApi
) {
    fun getProductos(): Flow<Resource<List<ProductoDto>>> = flow {

        emit(Resource.Loading())
        try{
            val tickets = productoApi.getProductos()
            emit(Resource.Success(tickets))
        }catch (e: Exception){
            Log.e("Error", e.message.toString())
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
    fun getProductos(categoria: String): Flow<Resource<List<ProductoDto>>> = flow {

        emit(Resource.Loading())
        try{
            val tickets = productoApi.getProductos(categoria)
            emit(Resource.Success(tickets))
        }catch (e: Exception){
            Log.e("Error", e.message.toString())
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
    suspend fun saveProducto(producto: ProductoDto){
        try {
            productoApi.saveProducto(producto)
        }

        catch (e: Exception){

        }
    }
    suspend fun updateProducto(producto: ProductoDto){
        try {
            productoApi.updateProducto(producto.productoId, producto)
        }

        catch (e: Exception){

        }
    }
    suspend fun deleteProducto(producto: ProductoDto){
        try {
            productoApi.deleteProducto(producto.productoId)
        }

        catch (e: Exception){

        }
    }

    suspend fun getProducto(id: Int): ProductoDto? {
        return try {
            productoApi.getProducto(id)
        } catch (e: Exception) {
            e.message?.let { Log.e( "Error", it) }
            null
        }
    }

}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}