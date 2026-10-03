package com.autoformation.pharmaciesdegardes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.autoformation.pharmaciesdegardes.data.model.Favori
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriDao {
    @Query("SELECT * FROM favoris")
    fun getAllFavoris(): Flow<List<Favori>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favori: Favori): Long

    @Delete
    suspend fun delete(favori: Favori)
}
