package com.ucne.geekmarket.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PromocionDao {
    @Upsert
    suspend fun save( promocion: PromocionEntity)

    @Delete
    suspend fun delete(promocion: PromocionEntity)

    @Query(
        """
            SELECT * 
            FROM Promociones
            WHERE promocionId = :id
            LIMIT 1
        """
    )
    suspend fun find(id: Int): PromocionEntity?

    @Query("SELECT * FROM Promociones")
    fun getAll(): Flow<List<PromocionEntity>>

}