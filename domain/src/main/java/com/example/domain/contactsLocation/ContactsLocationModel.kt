package com.example.domain.contactsLocation

import com.example.domain.contactMap.ContactDataOwner

class ContactsLocationModel(private val contactDataOwner: ContactDataOwner) :
    ContactsLocationInteractor {
    override fun getDestinationContactData() = contactDataOwner.getAllContact()
}
