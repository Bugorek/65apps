package com.example.domain.contact_details

import java.util.*

interface BirthdayNotification {
    fun setNotification(id: String?, contactName: String?, monthOfBirth: Int?, dayOfBirth: Int?, nextBirthday: Calendar)
    fun deleteNotification(id: String?, contactName: String?)
    fun notificationStatus(id: String?, contactName: String?): Boolean
}