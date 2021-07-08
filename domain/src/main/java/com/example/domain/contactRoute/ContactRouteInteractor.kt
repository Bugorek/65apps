package com.example.domain.contactRoute

import com.example.domain.contactList.ShortContactModel
import com.example.domain.contactMap.SimpleMapData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ContactRouteInteractor {
    fun getContactData(query: String?): Single<List<ShortContactModel>>
    fun getDestinationContactData(): Single<List<SimpleMapData>>
    fun getRouteData(
        origin: DestinationModel,
        destination: DestinationModel
    ): Observable<List<SimplePoint>>
}