package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.dao.MedicamentDao
import com.autoformation.pharmaciesdegardes.data.model.Medicament
import kotlinx.coroutines.flow.Flow

class MedicamentRepositoryImpl(private val medicamentDao: MedicamentDao) : IMedicamentRepository {
    override fun getAllMedicaments(): Flow<List<Medicament>> {
        return medicamentDao.getAllMedicaments()
    }

    override suspend fun insert(medicament: Medicament): Long {
        return medicamentDao.insert(medicament)
    }
}
