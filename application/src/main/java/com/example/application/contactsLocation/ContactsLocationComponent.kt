package com.example.application.contactsLocation

import com.a65apps.pandaananass.tetsapplication.api.ContactsLocationContainer
import dagger.Subcomponent

@ContactsLocationScope
@Subcomponent(modules = [ContactsLocationModule::class])
interface ContactsLocationComponent : ContactsLocationContainer {
    @Subcomponent.Factory
    interface Factory : ContactsLocationContainer.Factory {
        override fun create(): ContactsLocationContainer
    }
}
