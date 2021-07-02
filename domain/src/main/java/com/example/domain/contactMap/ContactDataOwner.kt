package com.example.domain.contactMap

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ContactDataOwner {
    fun addContact(contact: SimpleMapData): Completable

    fun updateContact(contact: SimpleMapData): Completable

    fun getContactById(contactId: String): Single<SimpleMapData>
}
