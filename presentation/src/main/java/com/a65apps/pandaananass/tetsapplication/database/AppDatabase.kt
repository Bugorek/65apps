package com.a65apps.pandaananass.tetsapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MapDataEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): MapDataDao
}
