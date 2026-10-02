package com.autoformation.pharmaciesdegardes.data.repository

import com.autoformation.pharmaciesdegardes.data.dao.FavoriDao
import com.autoformation.pharmaciesdegardes.data.model.Favori
import kotlinx.coroutines.flow.Flow

class FavoriRepositoryImpl(private val favoriDao: FavoriDao) : IFavoriRepository {
    override fun getAllFavoris(): Flow<List<Favori>> {
        return favoriDao.getAllFavoris()
    }

    override suspend fun insert(favori: Favori): Long {
        return favoriDao.insert(favori)
    }

    override suspend fun delete(favori: Favori) {
        favoriDao.delete(favori)
    }
}
