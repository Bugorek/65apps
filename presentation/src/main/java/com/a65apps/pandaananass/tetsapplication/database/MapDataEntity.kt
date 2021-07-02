package com.a65apps.pandaananass.tetsapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.contactMap.SimpleMapData

@Entity(tableName = "contacts")
data class MapDataEntity(
    @PrimaryKey
    val id: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
