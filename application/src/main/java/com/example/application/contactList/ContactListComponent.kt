package com.example.application.contactList

import com.a65apps.pandaananass.tetsapplication.api.ContactListContainer
import dagger.Subcomponent

@ContactListScope
@Subcomponent(modules = [ContactListModule::class])
interface ContactListComponent : ContactListContainer {
    @Subcomponent.Factory
    interface Factory : ContactListContainer.Factory {
        override fun create(): ContactListComponent
    }
}
