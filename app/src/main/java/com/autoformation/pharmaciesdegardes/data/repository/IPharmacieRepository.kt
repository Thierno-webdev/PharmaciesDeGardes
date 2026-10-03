package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import kotlinx.coroutines.flow.Flow

interface IPharmacieRepository {
    fun getAllPharmacies(): Flow<List<Pharmacie>>
    fun getPharmaciesDeGarde(): Flow<List<Pharmacie>>
    suspend fun insert(pharmacie: Pharmacie): Long
}
