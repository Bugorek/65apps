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
        contactName: String?,
        monthOfBirth: Int?,
        dayOfBirth: Int?,
        nextBirthday: Calendar
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = contactName?.let { getIntent(contactId = id, contactName = it) }
        val pendingIntent =
            id?.let { it -> PendingIntent.getBroadcast(context, it.hashCode(), intent, 0) }
        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
    }

    override fun deleteNotification(id: String?, contactName: String?) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = contactName?.let { getIntent(contactId = id, contactName = it) }
        val pendingIntent =
            id?.let { it -> PendingIntent.getBroadcast(context, it.hashCode(), intent, 0) }
        alarmManager?.cancel(pendingIntent)
        pendingIntent?.cancel()
    }

    override fun notificationStatus(
        id: String?,
        contactName: String?
    ): Boolean {
        val intent = contactName?.let { getIntent(contactId = id, contactName = it) }
        val status = id?.let {
            PendingIntent.getBroadcast(
                context,
                it.hashCode(),
                intent,
                PendingIntent.FLAG_NO_CREATE
            )
        }
        return status != null
    }

    private fun getIntent(contactId: String?, contactName: String) =
        Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ARGUMENT_ID, contactId)
            putExtra(ARGUMENT_NAME, contactName)
            action = context.getString(R.string.notification_action)
        }
}