package com.example.domain.birthday_notification

import com.example.domain.contact_details.BirthdayNotification
import java.util.*

class BirthdayNotificationModel(
    private val birthdayNotificationHelper: BirthdayNotification,
    private val calendarDataProvider: CalendarRepository
) :
    BirthdayNotificationInteractor {
    override fun notificationClick(
        id: String?,
        contactName: String?,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        if (notificationStatus(id = id, contactName = contactName) == false) {
            if (monthOfBirth != null && dayOfBirth != null) {
                birthdayNotificationHelper.setNotification(
                    id = id,
                    contactName = contactName,
                    monthOfBirth = monthOfBirth,
                    dayOfBirth = dayOfBirth,
                    nextBirthday = birthdayCalendar(
                        dayOfBirth = dayOfBirth,
                        monthOfBirth = monthOfBirth
                    )
                )
            }
        } else {
            contactName?.let {
                birthdayNotificationHelper.deleteNotification(
                    id = id,
                    contactName = it
                )
            }
        }
    }

    override fun notificationStatus(id: String?, contactName: String?) =
        contactName?.let {
            birthdayNotificationHelper.notificationStatus(
                id = id,
                contactName = it
            )
        }

    private fun birthdayCalendar(dayOfBirth: Int, monthOfBirth: Int): Calendar {
        val calendar = Calendar.getInstance()
        val day = calendarDataProvider.getDate().get(Calendar.DAY_OF_MONTH)
        val month = calendarDataProvider.getDate().get(Calendar.MONTH)
        val year = calendarDataProvider.getDate().get(Calendar.YEAR)
        if (month - monthOfBirth + 1 < 0) {
            calendar.set(Calendar.YEAR, year)
        } else if (month - monthOfBirth + 1 == 0) {
            if (day - dayOfBirth > 0) {
                calendar.set(Calendar.YEAR, year + 1)
            } else {
                calendar.set(Calendar.YEAR, year)
            }
        } else {
            calendar.set(Calendar.YEAR, year + 1)
        }
        calendar.set(Calendar.MONTH, monthOfBirth - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfBirth)
        calendar.set(Calendar.MILLISECOND, 0)
        if (dayOfBirth == 29 && monthOfBirth == 2) {
            if (year % 4 == 0) {
                calendar.set(Calendar.YEAR, year + 4)
            } else if ((year + 1) % 4 == 0) {
                calendar.set(Calendar.YEAR, year + 1)
            }
        }
        return calendar
    }
}