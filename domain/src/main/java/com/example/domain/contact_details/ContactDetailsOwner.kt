package com.example.domain.contact_details

import io.reactivex.rxjava3.core.Single

interface ContactDetailsOwner {
    fun getContactDetails(id: String): Single<FullContactModel>
}