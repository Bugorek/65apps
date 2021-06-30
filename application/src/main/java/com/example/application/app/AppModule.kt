package com.example.application.app

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.repository.ContactDataSource
import com.example.domain.contactDetails.ContactDetailsOwner
import com.example.domain.contactList.ContactListOwner
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContactListRepository(context: Context): ContactListOwner =
        ContactDataSource(context)

    @Provides
    @Singleton
    fun provideContactDetailsRepository(context: Context): ContactDetailsOwner =
        ContactDataSource(context)
}
