package com.example.domain.contactDetails

import io.reactivex.rxjava3.core.Single

interface ContactDetailsInteractor {
    fun getContactData(id: String): Single<FullContactModel>
    fun notificationClick(contactModel: FullContactModel)
    fun notificationStatus(id: String?, contactName: String?): Boolean?
}
