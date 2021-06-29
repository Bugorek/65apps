package com.example.domain.contactDetails

import java.util.Calendar

interface BirthdayNotification {
    fun setNotification(contactModel: FullContactModel, nextBirthday: Calendar)
    fun deleteNotification(id: String?, contactName: String?)
    fun notificationStatus(id: String?, contactName: String?): Boolean
}
