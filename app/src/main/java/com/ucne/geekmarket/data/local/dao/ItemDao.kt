package com.ucne.geekmarket.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.geekmarket.data.local.entities.ItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Upsert
    suspend fun save(item: ItemEntity)

    @Delete
    suspend fun delete(pitem: ItemEntity)

    @Query(
        """
            SELECT * 
            FROM Items
            WHERE itemId = :id
            LIMIT 1
        """
    )
    suspend fun find(id: Int): ItemEntity?

    @Query(
        """
            SELECT * 
            FROM Items
            WHERE carritoId = :id
        """
    )
    fun carritoItem(id: Int): Flow<List<ItemEntity>>?
    @Query(
        """
            SELECT * 
            FROM Items
            WHERE carritoId = :id
        """
    )
    suspend fun carritoItemSuspend(id: Int): List<ItemEntity>

    @Query(
        """ 
        SELECT * 
        FROM Items 
        WHERE productoId = :productoId AND carritoId = :carritoId 
        LIMIT 1
        """
    )
    suspend fun findItemByProducto(productoId: Int, carritoId: Int): ItemEntity?

    @Query(
        """
            SELECT COUNT(*) AS item_count
            FROM Items
            WHERE carritoId = (SELECT MAX(carritoId) FROM Carritos WHERE pagado = 0)
            Limit 1
        """
    )
    fun getItemsCount(): Flow<Int>

    @Query(
        """
            SELECT COUNT(*) AS item_count
            FROM Items
            WHERE carritoId = (SELECT MAX(carritoId) 
                                FROM Carritos 
                                WHERE pagado = 0 AND personaId = :personaId)
            Limit 1

        """
    )
    fun getItemsCountByPersona(personaId: Int): Flow<Int>
    @Query(
        """
            SELECT sum(monto) AS item_count
            FROM Items
            WHERE carritoId = :carritoId
            Limit 1
        """
    )
    suspend fun getMontoTotal(carritoId: Int): Double
    @Query(
        """
            SELECT EXISTS 
                (SELECT 1 
                 FROM Items 
                 WHERE productoId = :productoId AND carritoId = :carritoId)
        """
    )
    suspend fun itemExit(productoId: Int, carritoId: Int): Boolean
    @Query("""
        UPDATE Carritos
        SET total = :total
        WHERE carritoId = :carritoId
    """)
    suspend fun calcularTotal(carritoId: Int , total: Double)

    @Query("SELECT * FROM Items")
    fun getAll(): Flow<List<ItemEntity>>

}