package com.example.domain.contactMap

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContactMapInteractor {
    fun getContactAddress(latitude: Double, longitude: Double): Observable<ContactAddress?>

    fun saveContactData(contact: SimpleMapData): Completable

    fun updateContactData(contact: SimpleMapData): Completable

    fun getContact(id: String): Single<SimpleMapData>
}
