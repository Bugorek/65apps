package com.example.application.contactsLocation

import com.a65apps.pandaananass.tetsapplication.contactsLocation.ContactsLocationPresenter
import com.a65apps.pandaananass.tetsapplication.database.AppDatabase
import com.a65apps.pandaananass.tetsapplication.database.DatabaseOpenHelper
import com.example.domain.contactMap.ContactDataOwner
import com.example.domain.contactsLocation.ContactsLocationInteractor
import com.example.domain.contactsLocation.ContactsLocationModel
import dagger.Module
import dagger.Provides

@Module
class ContactsLocationModule {
    @Provides
    @ContactsLocationScope
    fun provideContactDataOwner(database: AppDatabase): ContactDataOwner =
        DatabaseOpenHelper(database.contactDao())

    @Provides
    @ContactsLocationScope
    fun provideContactsLocationModel(contactDataOwner: ContactDataOwner): ContactsLocationInteractor =
        ContactsLocationModel(contactDataOwner)

    @Provides
    @ContactsLocationScope
    fun provideContactsLocationPresenter(contactsLocationModel: ContactsLocationInteractor) =
        ContactsLocationPresenter(contactsLocationModel)
}
