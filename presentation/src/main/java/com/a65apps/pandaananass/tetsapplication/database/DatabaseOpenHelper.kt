package com.a65apps.pandaananass.tetsapplication.database

import com.example.domain.contactMap.ContactDataOwner
import com.example.domain.contactMap.SimpleMapData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class DatabaseOpenHelper(private val contactDao: MapDataDao) : ContactDataOwner {
    override fun addContact(contact: SimpleMapData): Completable =
        Completable.fromCallable {
            contactDao.addContact(contact.toMapData())
        }

    override fun updateContact(contact: SimpleMapData): Completable =
        Completable.fromCallable {
            contactDao.updateContact(contact.toMapData())
        }

    override fun getContactById(contactId: String): Single<SimpleMapData> =
        Single.fromCallable {
            contactDao.getContactById(contactId).toSimpleData()
        }

    override fun getAllContact(): Single<List<SimpleMapData>> =
        Single.fromCallable {
            contactDao.getAll().map { it.toSimpleData() }
        }


    private fun SimpleMapData.toMapData() = MapDataEntity(
        id = id,
        address = address,
        latitude = latitude,
        longitude = longitude
    )

    private fun MapDataEntity.toSimpleData() = SimpleMapData(
        id = id,
        address = address,
        latitude = latitude,
        longitude = longitude
    )
}
