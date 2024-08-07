package com.ucne.geekmarket.data.repository

import android.util.Log
import com.ucne.geekmarket.data.local.dao.ProductoDao
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.remote.api.ProductoApi
import com.ucne.geekmarket.data.remote.dto.ProductoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val productoApi: ProductoApi,
    private val productoDao: ProductoDao
) {
    fun getProductosDb()= productoDao.getAll()

    fun getProductoByCarritoId(id: Int)= productoDao.getProductoByCarritoId(id)

    suspend fun searchProducto(query: String) : List<ProductoEntity> {
        if(query.isEmpty()){
           return emptyList()
        }
        else{
            return productoDao.searchProducto(query)
        }
    }

    suspend fun getPoductosByCategoria(categoria: String) = productoDao.getSuspendByCategoria(categoria)


    fun loadToDb(): Flow<Resource<List<ProductoEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val productos = productoApi.getProductos()
            productos.forEach {
                productoDao.save(it.toEntity())
            }
            emit(Resource.Success(emptyList()))
        }catch (e: Exception){
            Log.e("Error", e.message.toString())
            emit(Resource.Error("Error de red: " + e.message.toString()))
        }
    }

    suspend fun getProducto(id: Int) = productoDao.find(id)

}

fun ProductoDto.toEntity() = ProductoEntity(
    productoId = productoId,
    nombre = nombre,
    precio = precio,
    descripcion = descripcion,
    categoria = categoria,
    imagen = imagen,
    stock = stocks?: 0,
    especificacion = especificacion?: ""
)

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}