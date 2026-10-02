package com.autoformation.pharmaciesdegardes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "stocks",
    foreignKeys = [
        ForeignKey(
            entity = Pharmacie::class,
            parentColumns = ["id"],
            childColumns = ["pharmacieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Medicament::class,
            parentColumns = ["id"],
            childColumns = ["medicamentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("pharmacieId"),
        Index("medicamentId")
    ]
)
data class Stock(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pharmacieId: Long,
    val medicamentId: Long,
    val disponible: Boolean
)
