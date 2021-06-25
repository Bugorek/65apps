package com.example.domain.birthday_notification

import java.util.*

class CalendarDataProvider : CalendarRepository {
    private var calendar: Calendar = Calendar.getInstance()

    override fun setDate(day: Int, month: Int, year: Int) {
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MILLISECOND, 0)
        }
    }

    override fun getDate() = calendar
}