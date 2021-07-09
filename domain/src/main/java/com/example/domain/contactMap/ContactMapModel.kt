package com.example.domain.contactMap

import io.reactivex.rxjava3.core.Observable

class ContactMapModel(
    private val contactAddressOwner: ContactAddressOwner,
    private val contactDataOwner: ContactDataOwner
) : ContactMapInteractor {
    override fun getContactAddress(latitude: Double, longitude: Double): Observable<ContactAddress?> =
        contactAddressOwner.getContactAddress(latitude, longitude)

    override fun saveContactData(contact: SimpleMapData) = contactDataOwner.addContact(contact)

    override fun updateContactData(contact: SimpleMapData) =
        contactDataOwner.updateContact(contact)

    override fun getContact(id: String) = contactDataOwner.getContactById(id)
}
