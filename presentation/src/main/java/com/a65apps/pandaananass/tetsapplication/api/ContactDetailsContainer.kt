package com.a65apps.pandaananass.tetsapplication.api

import com.a65apps.pandaananass.tetsapplication.contactDetails.ContactDetailsFragment

interface ContactDetailsContainer {
    fun inject(contactDetailsFragment: ContactDetailsFragment)

    interface Factory {
        fun create(): ContactDetailsContainer
    }
}
