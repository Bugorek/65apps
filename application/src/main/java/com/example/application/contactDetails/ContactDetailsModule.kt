package com.example.application.contactDetails

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.contactDetails.BirthdayNotificationHelper
import com.a65apps.pandaananass.tetsapplication.contactDetails.ContactDetailsPresenter
import com.example.domain.birthdayNotification.BirthdayNotificationInteractor
import com.example.domain.birthdayNotification.BirthdayNotificationModel
import com.example.domain.birthdayNotification.CalendarDataProvider
import com.example.domain.birthdayNotification.CalendarRepository
import com.example.domain.contactDetails.BirthdayNotification
import com.example.domain.contactDetails.ContactDetailsInteractor
import com.example.domain.contactDetails.ContactDetailsModel
import com.example.domain.contactDetails.ContactDetailsOwner
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @Provides
    @ContactDetailsScope
    fun provideBirthdayNotification(context: Context): BirthdayNotification =
        BirthdayNotificationHelper(context)

    @Provides
    @ContactDetailsScope
    fun provideCalendarDataProvide(): CalendarRepository =
        CalendarDataProvider()

    @Provides
    @ContactDetailsScope
    fun provideBirthdayNotificationInteractor(
        birthdayNotificationHelper: BirthdayNotification,
        calendarDataProvider: CalendarRepository
    ): BirthdayNotificationInteractor =
        BirthdayNotificationModel(birthdayNotificationHelper, calendarDataProvider)

    @Provides
    @ContactDetailsScope
    fun provideContactDetailsInterator(
        contactDataSource: ContactDetailsOwner,
        birthdayNotificationModel: BirthdayNotificationInteractor
    ): ContactDetailsInteractor =
        ContactDetailsModel(contactDataSource, birthdayNotificationModel)

    @Provides
    @ContactDetailsScope
    fun provideContactDetailsPresenter(contactDetailsInteractor: ContactDetailsInteractor): ContactDetailsPresenter {
        return ContactDetailsPresenter(contactDetailsInteractor)
    }
}
