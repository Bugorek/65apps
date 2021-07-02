package com.example.domain.contactMap

import io.reactivex.rxjava3.core.Observable

interface ContactAddressOwner {
    fun getContactAddress(latitude: Double, longitude: Double): Observable<ContactAddress>
}
