package com.ucne.geekmarket.data.repository

import android.util.Log
import com.ucne.geekmarket.data.local.dao.ProductoDao
import com.ucne.geekmarket.data.local.dao.PromocionDao
import com.ucne.geekmarket.data.local.entities.PromocionEntity
import com.ucne.geekmarket.data.remote.ProductoApi
import com.ucne.geekmarket.data.remote.PromocionApi
import com.ucne.geekmarket.data.remote.dto.PromocionDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
