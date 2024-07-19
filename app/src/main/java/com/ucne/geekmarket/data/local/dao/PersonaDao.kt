package com.ucne.geekmarket.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.geekmarket.data.local.entities.PersonaEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {
    @Upsert
    suspend fun save(persona: PersonaEntity)

    @Delete
    suspend fun delete(persona: PersonaEntity)

    @Query(
        """
            SELECT * 
            FROM Personas
            WHERE personaId = :id
            LIMIT 1
        """
    )
    suspend fun find(id: Int): PersonaEntity?

    @Query("SELECT * FROM Personas")
    fun getAll(): Flow<List<PersonaEntity>>

}