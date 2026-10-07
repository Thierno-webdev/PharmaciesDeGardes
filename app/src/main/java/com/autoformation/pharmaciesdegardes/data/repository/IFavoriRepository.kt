package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.model.Favori
import kotlinx.coroutines.flow.Flow

interface IFavoriRepository {
    fun getAllFavoris(): Flow<List<Favori>>
    suspend fun insert(favori: Favori): Long
    suspend fun delete(favori: Favori)
}
