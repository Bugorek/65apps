package com.a65apps.pandaananass.tetsapplication.contactMap

import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.rxjava3.core.Observable

interface GeocodingApi {
    @GET("maps/api/geocode/json?language=ru")
    fun getAddress(@Query("key") key: String, @Query("latlng") latlng: String): Observable<Result>
}