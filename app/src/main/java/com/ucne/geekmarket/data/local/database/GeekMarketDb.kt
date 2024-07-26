package com.ucne.geekmarket.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.geekmarket.data.local.dao.CarritoDao
import com.ucne.geekmarket.data.local.dao.ItemDao
import com.ucne.geekmarket.data.local.dao.PersonaDao
import com.ucne.geekmarket.data.local.dao.ProductoDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.ItemEntity
import com.ucne.geekmarket.data.local.entities.PersonaEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity

@Database(
    entities = [
        ProductoEntity::class,
        CarritoEntity::class,
        PersonaEntity::class,
        ItemEntity::class],

    version = 8,
    exportSchema = false
)
//@TypeConverters(ListConverter::class)
abstract class GeekMarketDb: RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun personaDao(): PersonaDao
    abstract fun itemDao(): ItemDao
}