package com.example.application.contactDetails

import com.a65apps.pandaananass.tetsapplication.api.ContactDetailsContainer
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class])
interface ContactDetailsComponent : ContactDetailsContainer {
    @Subcomponent.Factory
    interface Factory : ContactDetailsContainer.Factory {
        override fun create(): ContactDetailsComponent
    }
}
