package com.ucne.geekmarket.data.repository

import com.ucne.geekmarket.data.local.dao.PersonaDao
import com.ucne.geekmarket.data.local.entities.PersonaEntity
import com.ucne.geekmarket.data.remote.api.PersonaApi
import com.ucne.geekmarket.data.remote.dto.PersonaDto
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PersonaRepository @Inject constructor(
    private val personaDao: PersonaDao,
    private val personaApi: PersonaApi
) {
    suspend fun savePersonaSignUp(persona: PersonaEntity) {
        val savedPersona = personaApi.savePersonas(
            PersonaDto(
                nombre = persona.nombre,
                apellido = persona.apellido,
                fechaNacimiento = persona.fechaNacimiento,
                email = persona.email
            )
        )
        savedPersona?.let { personaToSave ->
            personaDao.save(
                PersonaEntity(
                    personaId = personaToSave.personaId,
                    nombre = personaToSave.nombre,
                    apellido = personaToSave.apellido,
                    fechaNacimiento = personaToSave.fechaNacimiento,
                    email = personaToSave.email
                )
            )
        }
    }

    suspend fun savePersona(persona: PersonaEntity) = personaDao.save(persona)

    fun getAllPersonas() = flow {
        emit(Resource.Loading())
        try {
            val persona = personaApi.getPersonas()
            persona.forEach {
                personaDao.save(
                    PersonaEntity(
                        personaId = it.personaId,
                        nombre = it.nombre,
                        apellido = it.apellido,
                        fechaNacimiento = it.fechaNacimiento,
                        email = it.email
                    )
                )
            }
            emit(Resource.Success(persona))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    suspend fun deletePersona(persona: PersonaEntity) = personaDao.delete(persona)

    suspend fun getPersona(id: Int) = personaDao.find(id)

    suspend fun getPersonaByEmail(email: String) = personaDao.getPersonaByEmail(email)

    suspend fun updatePersonaId(personaId: Int) {
        personaDao.updateCarritoPersonaId(personaId)
        personaDao.updateWishPersonaId(personaId)
    }

    fun getPersona() = personaDao.getAll()
}
