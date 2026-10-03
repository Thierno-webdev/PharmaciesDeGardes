package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.dao.StockDao
import com.autoformation.pharmaciesdegardes.data.model.Stock
import kotlinx.coroutines.flow.Flow

class StockRepositoryImpl(private val stockDao: StockDao) : IStockRepository {
    override fun getStocksForPharmacie(pharmacieId: Long): Flow<List<Stock>> {
        return stockDao.getStocksForPharmacie(pharmacieId)
    }

    override fun getStocksForMedicament(medicamentId: Long): Flow<List<Stock>> {
        return stockDao.getStocksForMedicament(medicamentId)
    }

    override suspend fun insert(stock: Stock): Long {
        return stockDao.insert(stock)
    }
}
