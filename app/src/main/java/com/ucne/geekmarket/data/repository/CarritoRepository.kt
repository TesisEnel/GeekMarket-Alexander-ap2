package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.CarritoDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import javax.inject.Inject


class CarritoRepository @Inject constructor(
    private val carritoDao: CarritoDao
) {
    suspend fun saveCarrito(carrito: CarritoEntity)= carritoDao.save(carrito)

    suspend fun deleteCarrito(carrito: CarritoEntity)= carritoDao.delete(carrito)

    suspend fun getCarritos(id: Int)= carritoDao.find(id)

    suspend fun getLastCarrito()= carritoDao.getLastCarrito()

    suspend fun getLastCarritoByPersona(personaId: Int)= carritoDao.getLastCarritoByPersona(personaId)

    fun getCarritos()= carritoDao.getAll()

}
