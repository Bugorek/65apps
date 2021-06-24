package com.example.domain.contact_list

import io.reactivex.rxjava3.core.Single

interface ContactListInteractor {
    fun getContactData(query: String?): Single<List<ShortContactModel>>

    fun updateContactData(query: String?): Single<List<ShortContactModel>>
}