package com.ucne.geekmarket.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.geekmarket.data.local.dao.ProductoDao
import com.ucne.geekmarket.data.local.entities.ProductoEntity

@Database(
    entities = [ProductoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GeekMarketDb: RoomDatabase() {
    abstract fun productoDao(): ProductoDao
}