package com.a65apps.pandaananass.tetsapplication.database

import com.example.domain.contactMap.ContactDataOwner
import com.example.domain.contactMap.SimpleMapData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class DatabaseOpenHelper(private val contactDao: MapDataDao) : ContactDataOwner {
    override fun addContact(contact: SimpleMapData): Completable =
        Completable.fromCallable {
            contactDao.addContact(contact.toEntity())
        }

    override fun updateContact(contact: SimpleMapData): Completable =
        Completable.fromCallable {
            contactDao.updateContact(contact.toEntity())
        }

    override fun getContactById(contactId: String): Single<SimpleMapData> =
        Single.fromCallable {
            contactDao.getContactById(contactId)
        }.map {
            SimpleMapData(
                id = it.id,
                address = it.address,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }

    private fun SimpleMapData.toEntity() = MapDataEntity(
        id = id,
        address = address,
        latitude = latitude,
        longitude = longitude
    )
}


