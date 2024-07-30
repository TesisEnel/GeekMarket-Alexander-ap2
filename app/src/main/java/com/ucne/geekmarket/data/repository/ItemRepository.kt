package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.ItemDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.ItemEntity
import javax.inject.Inject


class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository
) {
    suspend fun saveItem(item: ItemEntity) = itemDao.save(item)

    suspend fun deleteItem(item: ItemEntity) = itemDao.delete(item)

    suspend fun getItem(id: Int) = itemDao.find(id)

    suspend fun carritoItems(id: Int) = itemDao.CarritoItem(id)

    suspend fun itemExist(productoId: Int, carritoId: Int) =
        itemDao.itemExit(productoId, carritoId)

    suspend fun getItemByProducto(productoId: Int, carritoId: Int) =
        itemDao.findItemByProducto(productoId, carritoId)

    suspend fun AddItem(item: ItemEntity) {
        var lastCarrito = carritoRepository.getLastCarrito()
        if(lastCarrito == null){
            carritoRepository.saveCarrito(CarritoEntity(personaId = 1, pagado = false))
            lastCarrito = carritoRepository.getLastCarrito()
        }
        val existe = itemExist(item.productoId ?: 0, lastCarrito?.carritoId ?: 0)
        val producto = productoRepository.getProducto(item.productoId ?: 0)
        if (existe == false) {
            val monto = (item.cantidad?.toDouble() ?: 0.0) * (producto?.precio ?: 0.0)
            saveItem(
                ItemEntity(
                    carritoId = lastCarrito?.carritoId,
                    productoId = item.productoId,
                    cantidad = item.cantidad,
                    monto = monto
                )
            )
        } else {
            val itemsRepetido = getItemByProducto(item.productoId ?: 0, lastCarrito?.carritoId ?: 0)
            val cantidad = (itemsRepetido?.cantidad?: 0) + (item.cantidad ?: 0)
            val monto = cantidad * (producto?.precio ?: 0.0)
            saveItem(
                ItemEntity(
                    itemId = itemsRepetido?.itemId,
                    carritoId = lastCarrito?.carritoId,
                    productoId = item.productoId,
                    cantidad = cantidad,
                    monto = monto
                )
            )
        }
        val items = carritoItems(lastCarrito?.carritoId?:0)
        val total = items?.sumOf { (it.monto ?: 0.0)  }
        itemDao.calcularTotal(lastCarrito?.carritoId ?: 0, total ?: 0.0)
    }

    fun getItem() = itemDao.getAll()

    suspend fun getItemsCount () = itemDao.getItemsCount()
}
