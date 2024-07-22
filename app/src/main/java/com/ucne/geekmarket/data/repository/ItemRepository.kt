package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.ItemDao
import com.ucne.geekmarket.data.local.entities.ItemEntity
import javax.inject.Inject


class ItemRepository @Inject constructor(
    private val itemDao: ItemDao
) {
    suspend fun saveItem(item: ItemEntity)= itemDao.save(item)

    suspend fun deleteItem(item: ItemEntity)= itemDao.delete(item)

    suspend fun getItem(id: Int)= itemDao.find(id)

    suspend fun carritoItems(id: Int)= itemDao.CarritoItem(id)

    fun getItem()= itemDao.getAll()
}
