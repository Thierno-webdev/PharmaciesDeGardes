package com.autoformation.pharmaciesdegardes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.autoformation.pharmaciesdegardes.data.model.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks WHERE pharmacieId = :pharmacieId")
    fun getStocksForPharmacie(pharmacieId: Long): Flow<List<Stock>>

    @Query("SELECT * FROM stocks WHERE medicamentId = :medicamentId AND disponible = 1")
    fun getStocksForMedicament(medicamentId: Long): Flow<List<Stock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: Stock): Long
}
