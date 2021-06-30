package com.a65apps.pandaananass.tetsapplication

import android.app.AlarmManager
import android.app.PendingIntent
import com.example.domain.contactDetails.BirthdayNotification
import com.example.domain.contactDetails.FullContactModel
import java.util.Calendar

class BirthdayNotificationHelperPlug(
    private val pendingIntent: PendingIntent,
    private val alarmManager: AlarmManager
) : BirthdayNotification {
    private val notificationList = mutableListOf<FullContactModel>()

    override fun setNotification(
        contactModel: FullContactModel,
        nextBirthday: Calendar
    ) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
        notificationList.add(FullContactModel(contactModel.id))
    }

    override fun deleteNotification(id: String?, contactName: String?) {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    override fun notificationStatus(id: String?, contactName: String?) =
        notificationList.singleOrNull { it.id == id } != null
}
