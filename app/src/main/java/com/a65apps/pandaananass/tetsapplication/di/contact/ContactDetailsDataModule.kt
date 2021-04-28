package com.a65apps.pandaananass.tetsapplication.di.contact

import com.a65apps.pandaananass.tetsapplication.data.ContactDataSource
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactDetailsOwner
import dagger.Binds
import dagger.Module

@Module
interface ContactDetailsDataModule {
    @Binds
    @ContactDetailsScope
    fun setContactRepository(contactDetailsOwner: ContactDataSource): ContactDetailsOwner
}