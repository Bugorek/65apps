package com.example.domain

import com.example.domain.birthdayNotification.BirthdayNotificationInteractor
import com.example.domain.birthdayNotification.BirthdayNotificationModel
import com.example.domain.birthdayNotification.CalendarDataProvider
import com.example.domain.birthdayNotification.CalendarRepository
import com.example.domain.contactDetails.BirthdayNotification
import com.example.domain.contactDetails.FullContactModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.`verify`
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner::class)
class BirthdayNotificationModelTest {
    @Mock
    private lateinit var birthdayNotificationHelper: BirthdayNotification
    private lateinit var calendarDataProvider: CalendarRepository
    private lateinit var birthdayNotificationModel: BirthdayNotificationInteractor
    private var contact = FullContactModel(
        id = "1",
        name = "Иван Иванович",
        monthOfBirth = 9,
        dayOfBirth = 8
    )

    @Before
    fun before() {
        calendarDataProvider = CalendarDataProvider()
        birthdayNotificationModel =
            BirthdayNotificationModel(birthdayNotificationHelper, calendarDataProvider)
    }

    @Test
    fun createUncreatedNotificationForTheNextYear() {
        `when`(
            birthdayNotificationHelper.notificationStatus(contact.id, contact.name)
        ).thenReturn(false)
        calendarDataProvider.setDate(day = 9, month = Calendar.SEPTEMBER, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 8)
            set(Calendar.MILLISECOND, 0)
        }
        birthdayNotificationModel.notificationClick(contact)
        verify(birthdayNotificationHelper).setNotification(contact, nextBirthday)
    }

    @Test
    fun createUncreatedNotificationForTheCurrentYear() {
        `when`(
            birthdayNotificationHelper.notificationStatus(contact.id, contact.name)
        ).thenReturn(false)
        calendarDataProvider.setDate(day = 7, month = Calendar.SEPTEMBER, year = 1999)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1999)
            set(Calendar.MONTH, 8)
            set(Calendar.DAY_OF_MONTH, 8)
            set(Calendar.MILLISECOND, 0)
        }
        birthdayNotificationModel.notificationClick(contact)
        verify(birthdayNotificationHelper).setNotification(contact, nextBirthday)
    }

    @Test
    fun deleteTheCreatedNotification() {
        `when`(
            birthdayNotificationHelper.notificationStatus(contact.id, contact.name)
        ).thenReturn(true)
        birthdayNotificationModel.notificationClick(contact)
        verify(birthdayNotificationHelper).deleteNotification(contact.id, contact.name)
    }

    @Test
    fun addNotificationForPersonBornOnFebruaryTwentyNinthForTheNextYear() {
        `when`(
            birthdayNotificationHelper.notificationStatus(contact.id, contact.name)
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
        birthdayNotificationModel.notificationClick(contact)
        verify(birthdayNotificationHelper).setNotification(contact, nextBirthday)
    }

    @Test
    fun addNotificationForPersonBornOnFebruaryTwentyNinthInFourYears() {
        `when`(
            birthdayNotificationHelper.notificationStatus(contact.id, contact.name)
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
        birthdayNotificationModel.notificationClick(contact)
        verify(birthdayNotificationHelper).setNotification(contact, nextBirthday)
    }
}
