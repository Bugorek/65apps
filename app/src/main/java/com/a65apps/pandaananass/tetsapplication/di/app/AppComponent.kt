package com.a65apps.pandaananass.tetsapplication.di.app

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.app.AppDelegate
import com.a65apps.pandaananass.tetsapplication.di.contact.ContactDetailsComponent
import com.a65apps.pandaananass.tetsapplication.di.contacts.ContactListComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun provideContext(): Context
    fun plusContactListComponent(): ContactListComponent
    fun plusContactDetailsComponent(): ContactDetailsComponent
    fun inject(appDelegate: AppDelegate)
}