package com.a65apps.pandaananass.tetsapplication.contact_details

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.receivers.AlarmReceiver
import com.example.domain.contact_details.BirthdayNotification
import java.util.*

private const val ARGUMENT_ID = "Id"
private const val ARGUMENT_NAME = "Name"

class BirthdayNotificationHelper(private val context: Context) : BirthdayNotification {
    override fun setNotification(
        id: String?,
        contactName: String,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = getIntent(contactId = id, contactName = contactName)
        val pendingIntent =
            id?.let { it -> PendingIntent.getBroadcast(context, it.hashCode(), intent, 0) }
        if (monthOfBirth != null && dayOfBirth != null) {
            alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                birthdayCalendar(
                    dayOfBirth = dayOfBirth,
                    monthOfBirth = monthOfBirth
                ).timeInMillis,
                pendingIntent
            )
        }
    }

    override fun deleteNotification(id: String?, contactName: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = getIntent(contactId = id, contactName = contactName)
        val pendingIntent =
            id?.let { it -> PendingIntent.getBroadcast(context, it.hashCode(), intent, 0) }
        alarmManager?.cancel(pendingIntent)
        pendingIntent?.cancel()
    }

    private fun getIntent(contactId: String?, contactName: String) =
        Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ARGUMENT_ID, contactId)
            putExtra(ARGUMENT_NAME, contactName)
            action = context.getString(R.string.notification_action)
        }

    private fun birthdayCalendar(dayOfBirth: Int, monthOfBirth: Int): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val year = Calendar.getInstance().get(Calendar.YEAR)
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
        return calendar
    }
}