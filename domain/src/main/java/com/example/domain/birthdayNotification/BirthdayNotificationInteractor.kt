package com.example.domain.birthdayNotification

import com.example.domain.contactDetails.FullContactModel

interface BirthdayNotificationInteractor {
    fun notificationClick(contactModel: FullContactModel)

    fun notificationStatus(id: String?, contactName: String?): Boolean?
}
