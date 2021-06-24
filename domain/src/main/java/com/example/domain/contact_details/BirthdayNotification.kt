package com.example.domain.contact_details

interface BirthdayNotification {
    fun setNotification(id: String?, contactName: String, monthOfBirth: Int?, dayOfBirth: Int?)
    fun deleteNotification(id: String?, contactName: String)
}