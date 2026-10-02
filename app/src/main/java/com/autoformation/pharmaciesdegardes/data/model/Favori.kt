package com.autoformation.pharmaciesdegardes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "favoris",
    foreignKeys = [
        ForeignKey(
            entity = Pharmacie::class,
            parentColumns = ["id"],
            childColumns = ["pharmacieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("pharmacieId")
    ]
)
data class Favori(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pharmacieId: Long
)
