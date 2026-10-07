package com.autoformation.pharmaciesdegardes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.autoformation.pharmaciesdegardes.data.model.Medicament
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentDao {
    @Query("SELECT * FROM medicaments")
    fun getAllMedicaments(): Flow<List<Medicament>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicament: Medicament): Long
}
