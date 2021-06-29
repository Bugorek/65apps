package com.example.domain.birthdayNotification

import com.example.domain.contactDetails.BirthdayNotification
import com.example.domain.contactDetails.FullContactModel
import java.util.Calendar

private const val COEFFICIENT_LEAP_YEAR = 4
private const val NUMBER_OF_DAYS_IN_FEBRUARY_LEAP_YEAR = 29
private const val FEBRUARY_NUMBER = 2

class BirthdayNotificationModel(
    private val birthdayNotificationHelper: BirthdayNotification,
    private val calendarDataProvider: CalendarRepository
) :
    BirthdayNotificationInteractor {
    override fun notificationClick(contactModel: FullContactModel) {
        if (notificationStatus(contactModel.id, contactModel.name) == false) {
            contactModel.dayOfBirth?.let { dayOfBirth ->
                contactModel.monthOfBirth?.let { monthOfBirth ->
                    birthdayCalendar(dayOfBirth, monthOfBirth)
                }
            }?.let { nextBirthday ->
                birthdayNotificationHelper.setNotification(contactModel, nextBirthday)
            }
        } else {
            contactModel.name?.let { contactName ->
                birthdayNotificationHelper.deleteNotification(contactModel.id, contactName)
            }
        }
    }

    override fun notificationStatus(id: String?, contactName: String?) =
        contactName?.let {
            birthdayNotificationHelper.notificationStatus(id = id, it)
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
        if (dayOfBirth == NUMBER_OF_DAYS_IN_FEBRUARY_LEAP_YEAR && monthOfBirth == FEBRUARY_NUMBER) {
            if (year % COEFFICIENT_LEAP_YEAR == 0) {
                calendar.set(Calendar.YEAR, year + COEFFICIENT_LEAP_YEAR)
            } else if ((year + 1) % COEFFICIENT_LEAP_YEAR == 0) {
                calendar.set(Calendar.YEAR, year + 1)
            }
        }
        return calendar
    }
}
