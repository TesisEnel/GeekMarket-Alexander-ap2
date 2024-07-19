package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.PersonaDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.PersonaEntity
import javax.inject.Inject


class PersonaRepository @Inject constructor(
    private val personaDao: PersonaDao
) {
    suspend fun savePersona(persona: PersonaEntity)= personaDao.save(persona)

    suspend fun deletePersona(persona: PersonaEntity)= personaDao.delete(persona)

    suspend fun getPersona(id: Int)= personaDao.find(id)

    fun getPersona()= personaDao.getAll()
}
