package com.example.domain.contactDetails

import com.example.domain.birthdayNotification.BirthdayNotificationInteractor
import io.reactivex.rxjava3.core.Single

class ContactDetailsModel(
    private val contactDataSource: ContactDetailsOwner,
    private val birthdayNotificationModel: BirthdayNotificationInteractor
) : ContactDetailsInteractor {
    override fun getContactData(id: String): Single<FullContactModel> =
        contactDataSource.getContactDetails(id = id)

    override fun notificationClick(contactModel: FullContactModel) {
        contactModel.name?.let {
            birthdayNotificationModel.notificationClick(contactModel)
        }
    }

    override fun notificationStatus(id: String?, contactName: String?) =
        birthdayNotificationModel.notificationStatus(id, contactName)
}
