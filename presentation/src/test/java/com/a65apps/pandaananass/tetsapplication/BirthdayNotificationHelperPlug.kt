package com.a65apps.pandaananass.tetsapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.example.domain.contact_details.BirthdayNotification
import com.example.domain.contact_details.FullContactModel
import com.example.domain.contact_list.ShortContactModel
import java.util.*

class BirthdayNotificationHelperPlug(
    private val pendingIntent: PendingIntent,
    private val alarmManager: AlarmManager
) : BirthdayNotification {
    private val notificationList = mutableListOf<FullContactModel>()

    override fun setNotification(
        id: String?,
        contactName: String?,
        monthOfBirth: Int?,
        dayOfBirth: Int?,
        nextBirthday: Calendar
    ) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
        notificationList.add(FullContactModel(id = id))
    }

    override fun deleteNotification(id: String?, contactName: String?) {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    override fun notificationStatus(id: String?, contactName: String?): Boolean {
        var status: Boolean? = null
        notificationList.forEach {
            if (it.id == id) {
                status = true
            }
        }
        return status != null
    }
}