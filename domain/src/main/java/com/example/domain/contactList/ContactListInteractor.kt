package com.example.domain.contactList

import io.reactivex.rxjava3.core.Single

interface ContactListInteractor {
    fun getContactData(query: String?): Single<List<ShortContactModel>>

    fun updateContactData(query: String?): Single<List<ShortContactModel>>
}
