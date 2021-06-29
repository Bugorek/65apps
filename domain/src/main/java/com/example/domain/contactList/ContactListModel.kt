package com.example.domain.contactList

import io.reactivex.rxjava3.core.Single

class ContactListModel(private val contactDataSource: ContactListOwner) : ContactListInteractor {
    override fun getContactData(query: String?): Single<List<ShortContactModel>> =
        contactDataSource.getContactList(query)

    override fun updateContactData(query: String?): Single<List<ShortContactModel>> {
        return if (query == "") {
            contactDataSource.getContactList(null)
        } else {
            contactDataSource.getContactList(query)
        }
    }
}
