package com.a65apps.pandaananass.tetsapplication.contactRoute

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.contactList.ShortContactModel
import com.example.domain.contactMap.SimpleMapData
import com.example.domain.contactRoute.ContactRouteInteractor
import com.example.domain.contactRoute.DestinationModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class ContactRoutePresenter(private val contactRouteModel: ContactRouteInteractor) :
    MvpPresenter<ContactRouteView>() {
    fun getContactData(id: String) {
        contactRouteModel.getContactData(null)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                getDestinationData(id, it)
            }, {
                viewState.setEmptyList()
            })
    }

    fun getRouteData(origin: DestinationModel, destination: DestinationModel) {
        contactRouteModel.getRouteData(origin, destination)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewState.setRoute(it)
            }, {
                viewState.setRouteDataError()
            })
    }

    private fun getDestinationData(id: String, contactList: List<ShortContactModel>) {
        contactRouteModel.getDestinationContactData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                convertToDestinationData(id = id, contactData = contactList, contactMapData = it)
            }, {
                viewState.setEmptyList()
            })
    }

    private fun convertToDestinationData(
        id: String,
        contactData: List<ShortContactModel>,
        contactMapData: List<SimpleMapData>
    ) {
        val destinations: MutableList<DestinationModel> = mutableListOf()
        var currentContact: DestinationModel? = null
        for (contact in contactData) {
            for (mapData in contactMapData) {
                if (contact.id == mapData.id && contact.id != id) {
                    contact.name?.let {
                        destinations.add(
                            DestinationModel(
                                id = id,
                                contactName = it,
                                address = mapData.address,
                                latitude = mapData.latitude,
                                longitude = mapData.longitude
                            )
                        )
                    }
                }
                if (contact.id == mapData.id && contact.id == id) {
                    currentContact = contact.name?.let {
                        DestinationModel(
                            id = id,
                            contactName = it,
                            address = mapData.address,
                            latitude = mapData.latitude,
                            longitude = mapData.longitude
                        )
                    }
                }
            }
        }
        currentContact?.let { viewState.setDestinationList(destinations, it) }
    }
}
