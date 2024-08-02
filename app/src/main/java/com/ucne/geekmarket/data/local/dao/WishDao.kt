package com.ucne.geekmarket.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import com.ucne.geekmarket.data.local.entities.WishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishDao {
    @Upsert
    suspend fun save(WishList: WishEntity)

    @Delete
    suspend fun delete(WishList: WishEntity)

    @Query(
        """
            SELECT * 
            FROM Wishes
            WHERE wishId = :id
            LIMIT 1
        """
    )
    suspend fun find(id: Int): WishEntity?

    @Query(
        """
            SELECT p.*
            FROM Productos p
            JOIN Wishes w ON p.productoId = w.productoId
            WHERE w.personaId = :id;
        """
    )
    suspend fun productosByPersona(id: Int): List<ProductoEntity>?

    @Query(
        """ 
        SELECT * 
        FROM Wishes 
        WHERE productoId = :productoId 
        LIMIT 1
        """
    )
    suspend fun WishListByProducto(productoId: Int): WishEntity?

    @Query(
        """
            SELECT EXISTS 
                (SELECT 1 
                 FROM Wishes 
                 WHERE productoId = :productoId AND PersonaId = :PersonaId)
        """
    )
    suspend fun itemExit(productoId: Int, PersonaId: Int): Boolean

    @Query("SELECT * FROM Wishes")
    fun getAll(): Flow<List<WishEntity>>
}