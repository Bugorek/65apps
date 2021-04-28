package com.a65apps.pandaananass.tetsapplication.di.contact

import com.a65apps.pandaananass.tetsapplication.interfaces.ContactDetailsOwner
import com.a65apps.pandaananass.tetsapplication.presenters.ContactDetailsPresenter
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @Provides
    @ContactDetailsScope
    fun provideContactDetailsPresenter(contactDataSource: ContactDetailsOwner): ContactDetailsPresenter {
        return ContactDetailsPresenter(contactDataSource = contactDataSource)
    }
}