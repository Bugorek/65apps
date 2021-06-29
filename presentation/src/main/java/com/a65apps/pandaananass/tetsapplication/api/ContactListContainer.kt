package com.a65apps.pandaananass.tetsapplication.api

import com.a65apps.pandaananass.tetsapplication.contactList.ContactListFragment

interface ContactListContainer {
    fun inject(contactListFragment: ContactListFragment)

    interface Factory {
        fun create(): ContactListContainer
    }
}
