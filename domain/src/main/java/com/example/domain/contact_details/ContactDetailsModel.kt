package com.example.domain.contact_details

import com.example.domain.birthday_notification.BirthdayNotificationInteractor
import io.reactivex.rxjava3.core.Single

class ContactDetailsModel(
    private val contactDataSource: ContactDetailsOwner,
    private val birthdayNotificationModel: BirthdayNotificationInteractor
) : ContactDetailsInteractor {
    override fun getContactData(id: String): Single<FullContactModel> =
        contactDataSource.getContactDetails(id = id)

    override fun notificationClick(
        id: String?,
        contactName: String?,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        contactName?.let {
            birthdayNotificationModel.notificationClick(
                id = id,
                contactName = it,
                monthOfBirth = monthOfBirth,
                dayOfBirth = dayOfBirth
            )
        }
    }

    override fun notificationStatus(id: String?, contactName: String?) = birthdayNotificationModel.notificationStatus(id = id, contactName = contactName)
}