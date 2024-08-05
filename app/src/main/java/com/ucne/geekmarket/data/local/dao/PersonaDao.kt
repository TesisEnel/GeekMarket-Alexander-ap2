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

    @Query("UPDATE Carritos SET personaId = :personaId WHERE personaId = 0")
    suspend fun updateCarritoPersonaId(personaId: Int)

    @Query("UPDATE Wishes SET personaId = :personaId WHERE personaId = 0")
    suspend fun updateWishPersonaId(personaId: Int)

    @Query("SELECT * FROM Personas WHERE email LIKE  :email")
    suspend fun getPersonaByEmail(email: String): PersonaEntity?

    @Query("SELECT * FROM Personas")
    fun getAll(): Flow<List<PersonaEntity>>

}