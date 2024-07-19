package com.ucne.alexandersuarez_ap2_p1.data.local.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ucne.geekmarket.data.local.entities.Items
import com.ucne.geekmarket.data.remote.dto.ProductoDto

class ListConverter {
    @TypeConverter
    fun itemsListToString(items: List<Items>): String{
        val gson = Gson()
        return gson.toJson(items)
    }


    @TypeConverter
    fun stringToItemsList(itemsString: String): List<Items> {
        val itemsType = object : TypeToken<List<Items>>() {}.type
        return Gson().fromJson(itemsString, itemsType)
    }

    @TypeConverter
    fun productoToString(productoDto: ProductoDto): String{
        val gson = Gson()
        return gson.toJson(productoDto)
    }


    @TypeConverter
    fun stringToproducto(itemsString: String): ProductoDto {
        val itemsType = ProductoDto::class.java
        return Gson().fromJson(itemsString, itemsType)
    }



}