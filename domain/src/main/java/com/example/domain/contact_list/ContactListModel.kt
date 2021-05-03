package com.example.domain.contact_list

import io.reactivex.rxjava3.core.Single

class ContactListModel(private val contactDataSource: ContactListOwner) : ContactListInteractor {
    override fun getContactData(query: String?): Single<List<ShortContactModel>> =
        contactDataSource.getContactList(query = query)

    override fun updateContactData(query: String?): Single<List<ShortContactModel>> {
        return if (query == "") {
            contactDataSource.getContactList(query = null)
        } else {
            contactDataSource.getContactList(query = query)
        }
    }
}