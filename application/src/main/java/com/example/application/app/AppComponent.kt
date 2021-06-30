package com.example.application.app

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.api.AppContainer
import com.example.application.contactDetails.ContactDetailsComponent
import com.example.application.contactList.ContactListComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AppContainer {
    override val contactDetailsFactory: ContactDetailsComponent.Factory

    override val contactListFactory: ContactListComponent.Factory

    @Component.Factory
    interface Factory : AppContainer.Factory {
        override fun create(@BindsInstance context: Context): AppComponent
    }
}
