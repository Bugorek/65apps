package com.a65apps.pandaananass.tetsapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MapDataDao {
    @Insert
    fun addContact(contact: MapDataEntity)

    @Update
    fun updateContact(contact: MapDataEntity)

    @Query("SELECT * FROM contacts WHERE id = :contactId")
    fun getContactById(contactId: String): MapDataEntity

    @Query("SELECT * FROM contacts")
    fun getAll(): List<MapDataEntity>
}
