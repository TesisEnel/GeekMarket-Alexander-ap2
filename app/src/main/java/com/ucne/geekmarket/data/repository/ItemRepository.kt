package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.ItemDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.presentation.Common.AuthState
import javax.inject.Inject


class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val productoRepository: ProductoRepository,
    private val carritoRepository: CarritoRepository,
    private val personaRepository: PersonaRepository,
) {
    private suspend fun saveItem(item: ItemEntity) = itemDao.save(item)

    suspend fun deleteItem(item: ItemEntity) {
        val items = itemDao.carritoItemSuspend(item.carritoId ?: 0)
        itemDao.delete(item)
        val total = getMontoTotal(item.carritoId ?: 0)
        itemDao.calcularTotal(item.carritoId ?: 0, total)
    }

    suspend fun getMontoTotal(carritoId: Int) = itemDao.getMontoTotal(carritoId)

    suspend fun getItem(id: Int) = itemDao.find(id)

    fun carritoItems(id: Int) = itemDao.carritoItem(id)

    private suspend fun itemExist(productoId: Int, carritoId: Int) =
        itemDao.itemExit(productoId, carritoId)

    private suspend fun getItemByProducto(productoId: Int, carritoId: Int) =
        itemDao.findItemByProducto(productoId, carritoId)

    suspend fun AddItem(item: ItemEntity, authState: AuthState) {
        val persona = personaRepository.getPersonaByEmail(
            if (authState is AuthState.Authenticated)
                authState.email
            else ""
        )
        var lastCarrito = carritoRepository.getLastCarritoByPersona(persona?.personaId ?: 0)
        if (lastCarrito == null) {
            carritoRepository.saveCarrito(
                CarritoEntity(
                    personaId = persona?.personaId ?: 0,
                    pagado = false
                )
            )
            lastCarrito = carritoRepository.getLastCarritoByPersona(persona?.personaId ?: 0)
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
            val cantidad = (itemsRepetido?.cantidad ?: 0) + (item.cantidad ?: 0)
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
        val items = itemDao.carritoItemSuspend(lastCarrito?.carritoId ?: 0)
        val total = items.sumOf { (it.monto ?: 0.0) }
        itemDao.calcularTotal(lastCarrito?.carritoId ?: 0, total)
    }

    fun getItem() = itemDao.getAll()

    fun getItemsCount() = itemDao.getItemsCount()

    fun getItemsCountByPersona(personaId: Int) = itemDao.getItemsCountByPersona(personaId)
}
