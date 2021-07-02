package com.example.application.app

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.a65apps.pandaananass.tetsapplication.database.AppDatabase
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

    @Provides
    @Singleton
    fun provideContactDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "contacts"
        ).build()
}
