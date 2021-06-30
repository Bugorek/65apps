package com.example.domain.birthdayNotification

import java.util.Calendar

interface CalendarRepository {
    fun setDate(day: Int, month: Int, year: Int)
    fun getDate(): Calendar
}
