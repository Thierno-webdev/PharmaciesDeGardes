package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.dao.PharmacieDao
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import kotlinx.coroutines.flow.Flow

class PharmacieRepositoryImpl(private val pharmacieDao: PharmacieDao) : IPharmacieRepository {
    override fun getAllPharmacies(): Flow<List<Pharmacie>> {
        return pharmacieDao.getAllPharmacies()
    }

    override fun getPharmaciesDeGarde(): Flow<List<Pharmacie>> {
        return pharmacieDao.getPharmaciesDeGarde()
    }

    override suspend fun insert(pharmacie: Pharmacie): Long {
        return pharmacieDao.insert(pharmacie)
    }
}
