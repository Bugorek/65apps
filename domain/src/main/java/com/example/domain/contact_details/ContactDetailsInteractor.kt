package com.example.domain.contact_details

import io.reactivex.rxjava3.core.Single

interface ContactDetailsInteractor {
    fun getContactData(id: String): Single<FullContactModel>
    fun notificationClick(id: String?, contactName: String?, monthOfBirth: Int?, dayOfBirth: Int?)
    fun notificationStatus(id: String?, contactName: String?): Boolean?
}