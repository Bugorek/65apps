package com.example.domain.contactRoute

import com.example.domain.contactList.ContactListOwner
import com.example.domain.contactList.ShortContactModel
import com.example.domain.contactMap.ContactDataOwner
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class ContactRouteModel(
    private val contactDataSource: ContactListOwner,
    private val contactDataOwner: ContactDataOwner,
    private val contactRouteData: ContactRouteOwner
) : ContactRouteInteractor {
    override fun getContactData(query: String?) = contactDataSource.getContactList(null)

    override fun getDestinationContactData() = contactDataOwner.getAllContact()

    override fun getRouteData(
        origin: DestinationModel,
        destination: DestinationModel
    ) = contactRouteData.getContactRoute(origin, destination)
}