package com.example.domain.birthday_notification

import java.util.*

interface CalendarRepository {
    fun setDate(day: Int, month: Int, year: Int)
    fun getDate(): Calendar
}