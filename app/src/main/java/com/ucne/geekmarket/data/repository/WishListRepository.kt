package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.WishDao
import com.ucne.geekmarket.data.local.entities.WishEntity
import javax.inject.Inject


class WishListRepository @Inject constructor(
    private val wishDao: WishDao
) {
    suspend fun saveWish(wishList: WishEntity)=wishDao.save(wishList)

    suspend fun itemExist(productoId: Int, PersonaId: Int)= wishDao.itemExit(productoId, PersonaId)

    suspend fun deleteWish(wishList: WishEntity)= wishDao.delete(wishList)

    fun productosByPersona(personaId: Int)= wishDao.productosByPersona(personaId)

    suspend fun getWish(id: Int)= wishDao.find(id)

    suspend fun WishListByProducto(productoId: Int, personaId: Int)= wishDao.wishListByProducto(productoId, personaId)

    fun getWishes()= wishDao.getAll()
}
