package com.a65apps.pandaananass.tetsapplication.contactMap

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.contactMap.ContactAddress
import com.example.domain.contactMap.ContactMapInteractor
import com.example.domain.contactMap.SimpleMapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class ContactMapPresenter(private val contactMapModel: ContactMapInteractor) :
    MvpPresenter<ContactMapView>() {
    fun getContactAddress(latitude: Double, longitude: Double) {
        contactMapModel.getContactAddress(latitude, longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                setContactAddress(contactAddress = it, latitude = latitude, longitude = longitude)
            }, {
                setContactAddress(contactAddress = null, latitude = latitude, longitude = longitude)
            })
    }

    fun saveContactData(contactData: SimpleMapData) {
        contactMapModel.saveContactData(contactData)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewState.setContactDataStatus(false, contactData)
            }, {
                viewState.setContactDataError()
            })
    }

    fun updateContactData(contactData: SimpleMapData) {
        contactMapModel.updateContactData(contactData)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe({
                viewState.setContactDataStatus(true, contactData)
            }, {
                viewState.setContactDataError()
            })
    }

    fun getContact(id: String) {
        contactMapModel.getContact(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::setContact) {
                setContact(null)
            }
    }

    private fun setContact(contactData: SimpleMapData?) {
        viewState.setContact(contactData)
    }

    private fun setContactAddress(
        contactAddress: ContactAddress?,
        latitude: Double,
        longitude: Double
    ) {
        viewState.setContactAddress(
            contactAddress = contactAddress,
            latitude = latitude,
            longitude = longitude
        )
    }
}