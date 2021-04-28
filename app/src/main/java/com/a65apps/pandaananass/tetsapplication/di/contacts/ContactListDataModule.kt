package com.a65apps.pandaananass.tetsapplication.di.contacts

import com.a65apps.pandaananass.tetsapplication.data.ContactDataSource
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactListOwner
import dagger.Binds
import dagger.Module

@Module
interface ContactListDataModule {
    @Binds
    @ContactListScope
    fun setContactRepository(contactDataSource: ContactDataSource): ContactListOwner
}