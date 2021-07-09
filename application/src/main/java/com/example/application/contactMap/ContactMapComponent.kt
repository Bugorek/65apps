package com.example.application.contactMap

import com.a65apps.pandaananass.tetsapplication.api.ContactMapContainer
import dagger.Subcomponent

@ContactMapScope
@Subcomponent(modules = [ContactMapModule::class])
interface ContactMapComponent : ContactMapContainer {
    @Subcomponent.Factory
    interface Factory : ContactMapContainer.Factory {
        override fun create(): ContactMapContainer
    }
}