package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.model.Medicament
import kotlinx.coroutines.flow.Flow

interface IMedicamentRepository {
    fun getAllMedicaments(): Flow<List<Medicament>>
    suspend fun insert(medicament: Medicament): Long
}
