package com.a65apps.pandaananass.tetsapplication.di.contacts

import com.a65apps.pandaananass.tetsapplication.interfaces.ContactListOwner
import com.a65apps.pandaananass.tetsapplication.presenters.ContactListPresenter
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {
    @Provides
    @ContactListScope
    fun provideContactListPresenter(contactDataSource: ContactListOwner): ContactListPresenter {
        return ContactListPresenter(contactDataSource = contactDataSource)
    }
}