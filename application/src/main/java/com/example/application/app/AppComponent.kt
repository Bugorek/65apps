package com.example.application.app

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.api.AppContainer
import com.a65apps.pandaananass.tetsapplication.api.ContactRouteContainer
import com.example.application.contactDetails.ContactDetailsComponent
import com.example.application.contactList.ContactListComponent
import com.example.application.contactMap.ContactMapComponent
import com.example.application.contactRoute.ContactRouteComponent
import com.example.application.contactsLocation.ContactsLocationComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AppContainer {
    override val contactDetailsFactory: ContactDetailsComponent.Factory

    override val contactListFactory: ContactListComponent.Factory

    override val contactMapFactory: ContactMapComponent.Factory

    override val contactRouteFactory: ContactRouteComponent.Factory

    override val contactsLocationFactory: ContactsLocationComponent.Factory

    @Component.Factory
    interface Factory : AppContainer.Factory {
        override fun create(@BindsInstance context: Context): AppComponent
    }
}
