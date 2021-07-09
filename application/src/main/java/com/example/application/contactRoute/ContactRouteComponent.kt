package com.example.application.contactRoute

import com.a65apps.pandaananass.tetsapplication.api.ContactRouteContainer
import dagger.Subcomponent

@ContactRouteScope
@Subcomponent(modules = [ContactRouteModule::class])
interface ContactRouteComponent : ContactRouteContainer {
    @Subcomponent.Factory
    interface Factory : ContactRouteContainer.Factory {
        override fun create(): ContactRouteContainer
    }
}