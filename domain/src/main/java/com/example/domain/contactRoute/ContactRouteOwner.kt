package com.example.domain.contactRoute

import io.reactivex.rxjava3.core.Observable

interface ContactRouteOwner {
    fun getContactRoute(
        origin: DestinationModel,
        destinationModel: DestinationModel
    ): Observable<List<SimplePoint>>
}