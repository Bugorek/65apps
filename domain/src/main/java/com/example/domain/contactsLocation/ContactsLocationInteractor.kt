package com.example.domain.contactsLocation

import com.example.domain.contactMap.SimpleMapData
import io.reactivex.rxjava3.core.Single

interface ContactsLocationInteractor {
    fun getDestinationContactData(): Single<List<SimpleMapData>>
}
