package com.a65apps.pandaananass.tetsapplication

import android.app.AlarmManager
import android.app.PendingIntent
import com.a65apps.pandaananass.tetsapplication.contactDetails.ContactDetailsPresenter
import com.example.domain.birthdayNotification.BirthdayNotificationInteractor
import com.example.domain.birthdayNotification.BirthdayNotificationModel
import com.example.domain.birthdayNotification.CalendarDataProvider
import com.example.domain.birthdayNotification.CalendarRepository
import com.example.domain.contactDetails.BirthdayNotification
import com.example.domain.contactDetails.ContactDetailsInteractor
import com.example.domain.contactDetails.ContactDetailsModel
import com.example.domain.contactDetails.ContactDetailsOwner
import com.example.domain.contactDetails.FullContactModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner::class)
class BirthdayNotificationModelTest {
    @Mock
    private lateinit var pendingIntent: PendingIntent

    @Mock
    private lateinit var alarmManager: AlarmManager

    @Mock
    private lateinit var calendarDataProvider: CalendarRepository

    @Mock
    private lateinit var contactDataSource: ContactDetailsOwner
    private lateinit var contactDetailsPresenter: ContactDetailsPresenter
    private lateinit var contactDetailsModel: ContactDetailsInteractor
    private lateinit var birthdayNotificationModel: BirthdayNotificationInteractor
    private lateinit var birthdayNotificationHelper: BirthdayNotification
    private var contact = FullContactModel(
        id = "1",
        name = "Иван Иванович",
        monthOfBirth = 9,
        dayOfBirth = 8
    )

    @Before
    fun before() {
        birthdayNotificationHelper = BirthdayNotificationHelperPlug(pendingIntent, alarmManager)
        calendarDataProvider = CalendarDataProvider()
        birthdayNotificationModel =
            BirthdayNotificationModel(birthdayNotificationHelper, calendarDataProvider)
        contactDetailsModel = ContactDetailsModel(contactDataSource, birthdayNotificationModel)
        contactDetailsPresenter = ContactDetailsPresenter(contactDetailsModel)
    }

    @Test
    fun createUncreatedNotificationForTheNextYear() {
        calendarDataProvider.setDate(day = 9, month = Calendar.SEPTEMBER, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 8)
            set(Calendar.MILLISECOND, 0)
        }
        contactDetailsPresenter.notificationClick(contact)
        verify(alarmManager).setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
    }

    @Test
    fun createUncreatedNotificationForTheCurrentYear() {
        calendarDataProvider.setDate(day = 7, month = Calendar.SEPTEMBER, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1999)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 8)
            set(Calendar.MILLISECOND, 0)
        }
        contactDetailsPresenter.notificationClick(contact)
        verify(alarmManager).setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
    }

    @Test
    fun deleteTheCreatedNotification() {
        calendarDataProvider.setDate(day = 7, month = Calendar.SEPTEMBER, year = 1999)
        contactDetailsPresenter.notificationClick(contact)
        contactDetailsPresenter.notificationClick(contact)
        verify(alarmManager).cancel(pendingIntent)
    }

    @Test
    fun addNotificationForPersonBornOnFebruaryTwentyNinthForTheNextYear() {
        contact =
            FullContactModel(id = "1", name = "Иван Иванович", monthOfBirth = 2, dayOfBirth = 29)
        calendarDataProvider.setDate(day = 2, month = Calendar.MARCH, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DAY_OF_MONTH, 29)
            set(Calendar.MILLISECOND, 0)
        }
        contactDetailsPresenter.notificationClick(contact)
        verify(alarmManager).setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
    }

    @Test
    fun addNotificationForPersonBornOnFebruaryTwentyNinthInFourYears() {
        contact =
            FullContactModel(id = "1", name = "Иван Иванович", monthOfBirth = 2, dayOfBirth = 29)
        calendarDataProvider.setDate(day = 1, month = Calendar.MARCH, year = 2000)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2004)
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DAY_OF_MONTH, 29)
            set(Calendar.MILLISECOND, 0)
        }
        contactDetailsPresenter.notificationClick(contact)
        verify(alarmManager).setExact(
            AlarmManager.RTC_WAKEUP,
            nextBirthday.timeInMillis,
            pendingIntent
        )
    }
}
