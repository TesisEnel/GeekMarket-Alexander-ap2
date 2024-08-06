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
    suspend fun save(wishList: WishEntity)

    @Delete
    suspend fun delete(wishList: WishEntity)

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
            SELECT COUNT(*) AS item_count
            FROM Wishes
            WHERE personaId = :personaId
            Limit 1
        """
    )
    fun getWishCountByPersona(personaId: Int): Flow<Int>

    @Query(
        """
            SELECT p.*
            FROM Productos p
            JOIN Wishes w ON p.productoId = w.productoId
            WHERE w.personaId = :personaId;
        """
    )
    fun productosByPersona(personaId: Int): Flow<List<ProductoEntity>>?

    @Query(
        """ 
        SELECT * 
        FROM Wishes 
        WHERE productoId = :productoId AND  PersonaId = :personaId
        LIMIT 1
        """
    )
    suspend fun wishListByProducto(productoId: Int, personaId: Int): WishEntity?

    @Query(
        """
            SELECT EXISTS 
                (SELECT 1 
                 FROM Wishes 
                 WHERE productoId = :productoId AND PersonaId = :personaId)
        """
    )
    suspend fun itemExit(productoId: Int, personaId: Int): Boolean

    @Query("SELECT * FROM Wishes")
    fun getAll(): Flow<List<WishEntity>>
}