package com.a65apps.pandaananass.tetsapplication.di.contact

import com.a65apps.pandaananass.tetsapplication.fragments.ContactDetailsFragment
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class, ContactDetailsDataModule::class])
interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}