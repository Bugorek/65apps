package com.a65apps.pandaananass.tetsapplication.api

import com.a65apps.pandaananass.tetsapplication.contactMap.ContactMapFragment

interface ContactMapContainer {
    fun inject(contactMapFragment: ContactMapFragment)

    interface Factory {
        fun create(): ContactMapContainer
    }
}