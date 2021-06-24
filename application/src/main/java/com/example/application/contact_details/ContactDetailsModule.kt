package com.example.application.contact_details

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.contact_details.BirthdayNotificationHelper
import com.a65apps.pandaananass.tetsapplication.contact_details.ContactDetailsPresenter
import com.example.domain.contact_details.BirthdayNotification
import com.example.domain.contact_details.ContactDetailsInteractor
import com.example.domain.contact_details.ContactDetailsModel
import com.example.domain.contact_details.ContactDetailsOwner
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @Provides
    @ContactDetailsScope
    fun provideBirthdayNotification(context: Context): BirthdayNotification =
        BirthdayNotificationHelper(context = context)

    @Provides
    @ContactDetailsScope
    fun provideContactDetailsInterator(
        contactDataSource: ContactDetailsOwner,
        birthdayNotification: BirthdayNotification
    ): ContactDetailsInteractor =
        ContactDetailsModel(
            contactDataSource = contactDataSource,
            birthdayNotificationHelper = birthdayNotification
        )

    @Provides
    @ContactDetailsScope
    fun provideContactDetailsPresenter(contactDetailsInteractor: ContactDetailsInteractor): ContactDetailsPresenter {
        return ContactDetailsPresenter(contactDetailsModel = contactDetailsInteractor)
    }
}