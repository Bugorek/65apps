package com.example.domain.contactList

import io.reactivex.rxjava3.core.Single

interface ContactListOwner {
    fun getContactList(query: String?): Single<List<ShortContactModel>>
}
