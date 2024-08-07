package com.ucne.geekmarket.data.repository

import android.util.Log
import com.ucne.geekmarket.data.local.dao.PromocionDao
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.remote.api.PromocionApi
import com.ucne.geekmarket.data.remote.dto.PromocionDto
import javax.inject.Inject

class PromcionRepository @Inject constructor(
    private val promocionApi: PromocionApi,
    private val promocionDao: PromocionDao,
) {
    fun getPromocionesDb()= promocionDao.getAll()

    suspend fun getApiToDb(){
        try {
            val promociones = promocionApi.getPromociones()
            promociones.forEach {
                promocionDao.save(it.toEntity())
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }
}

fun PromocionDto.toEntity() = PromocionEntity(
    promocionId = promocionId,
    productoId = productoId,
    imagen = imagen
)
