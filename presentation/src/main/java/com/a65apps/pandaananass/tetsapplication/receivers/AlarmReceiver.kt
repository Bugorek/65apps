package com.a65apps.pandaananass.tetsapplication.receivers

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import java.util.Calendar

private const val ARGUMENT_ID = "Id"
private const val ARGUMENT_NAME = "Name"
private const val CHANNEL_ID = "channelID"
private const val CHANNEL_NAME = "channelName"
private const val LEAP_YEAR_DAY = 366
private const val NOT_LEAP_YEAR_DAY = 365
private const val COEFFICIENT_LEAP_YEAR = 4

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == context?.resources?.getString(R.string.notification_action)) {
            val contactId = intent?.getStringExtra(ARGUMENT_ID)
            val contactName = intent?.getStringExtra(ARGUMENT_NAME)
            val intentMain = Intent(context, MainActivity::class.java)
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                contactId.hashCode(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChanel(context = context)
            }
            intentMain.putExtra(ARGUMENT_ID, contactId)
            val pendingNotificationIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intentMain)
                getPendingIntent(0, 0)
            }
            val notification = createNotification(
                context = context,
                pendingIntent = pendingNotificationIntent,
                contactName = contactName
            )
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0, notification)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                birthdayCalendar().timeInMillis,
                pendingIntent
            )
        }
    }

    private fun birthdayCalendar(): Calendar {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val countYearDay: Int = if (year % COEFFICIENT_LEAP_YEAR == 0) {
            LEAP_YEAR_DAY
        } else {
            NOT_LEAP_YEAR_DAY
        }
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_YEAR, countYearDay)
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
        }
    }

    private fun createChanel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                context.applicationContext?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        context: Context,
        pendingIntent: PendingIntent,
        contactName: String?
    ): Notification {
        return NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText("${context.getString(R.string.receiver_birthday_message)} $contactName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }
}
