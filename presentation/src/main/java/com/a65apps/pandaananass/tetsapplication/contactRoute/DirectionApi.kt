package com.a65apps.pandaananass.tetsapplication.contactRoute

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionApi {
    @GET("maps/api/directions/json?")
    fun getAddress(
        @Query("key") key: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String
    ): Observable<Routes>
}
