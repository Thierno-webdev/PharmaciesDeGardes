package com.autoformation.pharmaciesdegardes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.autoformation.pharmaciesdegardes.data.model.Pharmacie
import kotlinx.coroutines.flow.Flow

@Dao
interface PharmacieDao {
    @Query("SELECT * FROM pharmacies")
    fun getAllPharmacies(): Flow<List<Pharmacie>>

    @Query("SELECT * FROM pharmacies WHERE estDeGarde = 1")
    fun getPharmaciesDeGarde(): Flow<List<Pharmacie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pharmacie: Pharmacie): Long
}
