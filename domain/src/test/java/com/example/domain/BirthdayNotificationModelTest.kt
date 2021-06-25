package com.example.domain

import com.example.domain.birthday_notification.BirthdayNotificationInteractor
import com.example.domain.birthday_notification.BirthdayNotificationModel
import com.example.domain.birthday_notification.CalendarDataProvider
import com.example.domain.birthday_notification.CalendarRepository
import com.example.domain.contact_details.BirthdayNotification
import com.example.domain.contact_details.FullContactModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class BirthdayNotificationModelTest {
    @Mock
    private lateinit var birthdayNotificationHelper: BirthdayNotification
    private lateinit var calendarDataProvider: CalendarRepository
    private lateinit var birthdayNotificationModel: BirthdayNotificationInteractor
    private var contact =
        FullContactModel(id = "1", name = "Иван Иванович", monthOfBirth = 9, dayOfBirth = 8)

    @Before
    fun before() {
        calendarDataProvider = CalendarDataProvider()
        birthdayNotificationModel =
            BirthdayNotificationModel(
                birthdayNotificationHelper = birthdayNotificationHelper,
                calendarDataProvider = calendarDataProvider
            )
    }

    @Test
    fun createUncreatedNotificationForTheNextYear() {
        `when`(
            birthdayNotificationHelper.notificationStatus(
                id = contact.id,
                contactName = contact.name
            )
        ).thenReturn(false)
        calendarDataProvider.setDate(day = 9, month = Calendar.SEPTEMBER, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 8)
            set(Calendar.MILLISECOND, 0)
        }
        birthdayNotificationModel.notificationClick(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth
        )
        verify(birthdayNotificationHelper).setNotification(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth,
            nextBirthday = nextBirthday
        )
    }

    @Test
    fun createUncreatedNotificationForTheCurrentYear() {
        `when`(
            birthdayNotificationHelper.notificationStatus(
                id = contact.id,
                contactName = contact.name
            )
        ).thenReturn(false)
        calendarDataProvider.setDate(day = 7, month = Calendar.SEPTEMBER, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1999)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 8)
            set(Calendar.MILLISECOND, 0)
        }
        birthdayNotificationModel.notificationClick(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth
        )
        verify(birthdayNotificationHelper).setNotification(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth,
            nextBirthday = nextBirthday
        )
    }

    @Test
    fun deleteTheCreatedNotification() {
        `when`(
            birthdayNotificationHelper.notificationStatus(
                id = contact.id,
                contactName = contact.name
            )
        ).thenReturn(true)
        birthdayNotificationModel.notificationClick(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth
        )
        verify(birthdayNotificationHelper).deleteNotification(
            id = contact.id,
            contactName = contact.name
        )
    }

    @Test
    fun addNotificationForPersonBornOnFebruaryTwentyNinthForTheNextYear() {
        `when`(
            birthdayNotificationHelper.notificationStatus(
                id = contact.id,
                contactName = contact.name
            )
        ).thenReturn(false)
        contact =
            FullContactModel(id = "1", name = "Иван Иванович", monthOfBirth = 2, dayOfBirth = 29)
        calendarDataProvider.setDate(day = 2, month = Calendar.MARCH, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DAY_OF_MONTH, 29)
            set(Calendar.MILLISECOND, 0)
        }
        birthdayNotificationModel.notificationClick(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth
        )
        verify(birthdayNotificationHelper).setNotification(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth,
            nextBirthday = nextBirthday
        )
    }

    @Test
    fun addNotificationForPersonBornOnFebruaryTwentyNinthInFourYears() {
        `when`(
            birthdayNotificationHelper.notificationStatus(
                id = contact.id,
                contactName = contact.name
            )
        ).thenReturn(false)
        contact =
            FullContactModel(id = "1", name = "Иван Иванович", monthOfBirth = 2, dayOfBirth = 29)
        calendarDataProvider.setDate(day = 1, month = Calendar.MARCH, year = 2000)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2004)
            set(Calendar.MONTH, Calendar.FEBRUARY)
            set(Calendar.DAY_OF_MONTH, 29)
            set(Calendar.MILLISECOND, 0)
        }
        birthdayNotificationModel.notificationClick(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth
        )
        verify(birthdayNotificationHelper).setNotification(
            id = contact.id,
            contactName = contact.name,
            monthOfBirth = contact.monthOfBirth,
            dayOfBirth = contact.dayOfBirth,
            nextBirthday = nextBirthday
        )
    }
}