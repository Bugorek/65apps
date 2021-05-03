package com.example.domain.contact_details

import io.reactivex.rxjava3.core.Single

class ContactDetailsModel(
    private val contactDataSource: ContactDetailsOwner,
    private val birthdayNotificationHelper: BirthdayNotification
) : ContactDetailsInteractor {
    override fun getContactData(id: String): Single<FullContactModel> =
        contactDataSource.getContactDetails(id = id)

    override fun setNotification(
        id: String?,
        contactName: String,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        birthdayNotificationHelper.setNotification(
            id = id,
            contactName = contactName,
            monthOfBirth = monthOfBirth,
            dayOfBirth = dayOfBirth
        )
    }

    override fun deleteNotification(id: String?, contactName: String) {
        birthdayNotificationHelper.deleteNotification(id = id, contactName = contactName)
    }
}