package com.autoformation.pharmaciesdegardes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicaments")
data class Medicament(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val categorie: String,
    val description: String,
    val prix: Double
)
