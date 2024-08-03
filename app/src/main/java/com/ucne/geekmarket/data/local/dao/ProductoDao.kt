package com.ucne.geekmarket.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.geekmarket.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Upsert
    suspend fun save(producto: ProductoEntity)

    @Delete
    suspend fun delete(producto: ProductoEntity)

    @Query(
        """
            SELECT * 
            FROM Productos
            WHERE productoId = :id
            LIMIT 1
        """
    )
    suspend fun find(id: Int): ProductoEntity?


    @Query("SELECT * FROM productos")
    fun getAll(): Flow<List<ProductoEntity>>



    @Query(
        """
            SELECT p.*
            FROM Productos p
            JOIN Items i ON p.productoId = i.productoId
            WHERE i.carritoId = :id;
        """
    )
    fun getProductoByCarritoId(id: Int): Flow<List<ProductoEntity>>
    @Query(
        """
            SELECT *
            FROM Productos p
            WHERE categoria = :categoria;
        """
    )
    fun getProductoByCategoria(categoria: String): Flow<List<ProductoEntity>>
    @Query(
        """
            SELECT *
            FROM Productos p
            WHERE categoria = :categoria;
        """
    )
    suspend fun getSuspendByCategoria(categoria: String): List<ProductoEntity>

    @Query("SELECT * FROM Productos WHERE nombre LIKE '%' || :query || '%'")
    suspend fun searchProducto(query: String): List<ProductoEntity>

}