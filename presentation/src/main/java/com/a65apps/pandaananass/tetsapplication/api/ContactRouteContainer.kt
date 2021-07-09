package com.a65apps.pandaananass.tetsapplication.api

import com.a65apps.pandaananass.tetsapplication.contactRoute.ContactRouteFragment

interface ContactRouteContainer {
    fun inject(contactRouteFragment: ContactRouteFragment)

    interface Factory {
        fun create(): ContactRouteContainer
    }
}