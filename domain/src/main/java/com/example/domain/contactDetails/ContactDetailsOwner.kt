package com.example.domain.contactDetails

import io.reactivex.rxjava3.core.Single

interface ContactDetailsOwner {
    fun getContactDetails(id: String): Single<FullContactModel>
}
