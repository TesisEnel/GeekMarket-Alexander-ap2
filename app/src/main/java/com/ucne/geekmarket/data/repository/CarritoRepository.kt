package com.ucne.geekmarket.data.repository

import android.util.Log
import com.ucne.geekmarket.data.local.dao.CarritoDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.remote.api.CarritoApi
import com.ucne.geekmarket.data.remote.dto.CarritoDto
import com.ucne.geekmarket.data.remote.dto.ItemDto
import javax.inject.Inject


class CarritoRepository @Inject constructor(
    private val carritoDao: CarritoDao,
    private val carritoApi: CarritoApi
) {
    suspend fun saveCarrito(carrito: CarritoEntity)= carritoDao.save(carrito)

    suspend fun deleteCarrito(carrito: CarritoEntity)= carritoDao.delete(carrito)

    suspend fun getCarritos(id: Int)= carritoDao.find(id)

    suspend fun getLastCarrito()= carritoDao.getLastCarrito()

    suspend fun getLastCarritoByPersona(personaId: Int)= carritoDao.getLastCarritoByPersona(personaId)

    fun getCarritos()= carritoDao.getAll()

    fun getAllCarritoByPersona(personaId: Int)= carritoDao.getAllByPersona(personaId)

//    fun getTickets(): Flow<Resource<List<TicketDto>>> = flow {
//
//        emit(Resource.Loading())
//        try{
//            val tickets = ticketsApi.getTickets()
//            emit(Resource.Success(tickets))
//        }catch (e: Exception){
//            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
//        }
//    }

    suspend fun saveCarritoApi(carritoDto: CarritoDto){
        try {
            val carritoSaved = carritoApi.saveCarrito(
                CarritoDto(
                    pagado = carritoDto.pagado,
                    total = carritoDto.total,
                    personaId = carritoDto.personaId,
                    items = carritoDto.items?.map {
                        ItemDto(
                            productoId = it.productoId,
                            cantidad = it.cantidad,
                            monto = it.monto
                        )
                    }
                )
            )
            carritoSaved?.let {
                carritoToSave ->
                carritoDao.save(
                    CarritoEntity(
                        carritoId = carritoToSave.carritoId,
                        pagado = carritoToSave.pagado,
                        total = carritoToSave.total,
                        personaId = carritoToSave.personaId
                    )
                )
            }

        }
        catch (e: Exception){
            Log.e("Error", e.message.toString())
        }
    }

}
