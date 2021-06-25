package com.example.domain.birthday_notification

interface BirthdayNotificationInteractor {
    fun notificationClick(
        id: String?,
        contactName: String?,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    )

    fun notificationStatus(id: String?, contactName: String?): Boolean?
}