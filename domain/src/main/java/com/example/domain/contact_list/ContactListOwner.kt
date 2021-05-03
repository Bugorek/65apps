package com.example.domain.contact_list

import io.reactivex.rxjava3.core.Single

interface ContactListOwner {
    fun getContactList(query: String?): Single<List<ShortContactModel>>
}