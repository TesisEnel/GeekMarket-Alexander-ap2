package com.ucne.geekmarket.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ucne.alexandersuarez_ap2_p1.data.local.database.converter.ListConverter
import com.ucne.geekmarket.data.local.dao.CarritoDao
import com.ucne.geekmarket.data.local.dao.PersonaDao
import com.ucne.geekmarket.data.local.dao.ProductoDao
import com.ucne.geekmarket.data.local.entities.CarritoEntity
import com.ucne.geekmarket.data.local.entities.Items
import com.ucne.geekmarket.data.local.entities.PersonaEntity
import com.ucne.geekmarket.data.local.entities.ProductoEntity

@Database(
    entities = [
        ProductoEntity::class,
        CarritoEntity::class,
        PersonaEntity::class,
        Items::class],

    version = 4,
    exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class GeekMarketDb: RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao
    abstract fun personaDao(): PersonaDao
}