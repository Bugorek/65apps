package com.a65apps.pandaananass.tetsapplication.di.contacts

import com.a65apps.pandaananass.tetsapplication.fragments.ContactListFragment
import dagger.Subcomponent

@ContactListScope
@Subcomponent(modules = [ContactListModule::class, ContactListDataModule::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}