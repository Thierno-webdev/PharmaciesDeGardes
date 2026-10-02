package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.model.Stock
import kotlinx.coroutines.flow.Flow

interface IStockRepository {
    fun getStocksForPharmacie(pharmacieId: Long): Flow<List<Stock>>
    fun getStocksForMedicament(medicamentId: Long): Flow<List<Stock>>
    suspend fun insert(stock: Stock): Long
}
