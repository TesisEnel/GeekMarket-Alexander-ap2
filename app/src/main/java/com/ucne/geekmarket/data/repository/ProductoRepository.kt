package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.ProductoDao
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val productoDao: ProductoDao
) {
    suspend fun savePorducto(producto: ProductoEntity)= productoDao.save(producto)

    suspend fun deleteProducto(producto: ProductoEntity)= productoDao.delete(producto)

    suspend fun getProducto(id: Int)= productoDao.find(id)

    fun getProductoss()= productoDao.getAll()

}