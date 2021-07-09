package com.a65apps.pandaananass.tetsapplication.contactMap

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.R
import com.example.domain.contactMap.ContactAddress
import com.example.domain.contactMap.ContactAddressOwner
import io.reactivex.rxjava3.core.Observable

class ContactAddressData(private val context: Context, private val geocodingApi: GeocodingApi) :
    ContactAddressOwner {
    override fun getContactAddress(
        latitude: Double,
        longitude: Double
    ): Observable<ContactAddress?> = geocodingApi.getAddress(
        context.resources.getString(R.string.google_maps_key),
        "$latitude,$longitude"
    ).map {
        if (it.result.isNotEmpty()) {
            ContactAddress(it.result[0].formattedAddress)
        } else {
            null
        }
    }
}