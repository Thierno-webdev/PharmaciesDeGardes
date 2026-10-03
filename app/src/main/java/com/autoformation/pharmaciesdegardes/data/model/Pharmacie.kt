package com.autoformation.pharmaciesdegardes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pharmacies")
data class Pharmacie(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val commune: String,
    val quartier: String,
    val telephone: String,
    val latitude: Double,
    val longitude: Double,
    val estDeGarde: Boolean,
    val debutGarde: String? = null,
    val finGarde: String? = null
)
