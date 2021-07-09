package com.a65apps.pandaananass.tetsapplication.api

import com.a65apps.pandaananass.tetsapplication.contactsLocation.ContactsLocationFragment

interface ContactsLocationContainer {
    fun inject(contactsLocationFragment: ContactsLocationFragment)

    interface Factory {
        fun create(): ContactsLocationContainer
    }
}
