package com.example.application.contactList

import com.a65apps.pandaananass.tetsapplication.contactList.ContactListPresenter
import com.example.domain.contactList.ContactListInteractor
import com.example.domain.contactList.ContactListModel
import com.example.domain.contactList.ContactListOwner
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {
    @Provides
    @ContactListScope
    fun provideContactListInteractor(contactDataSource: ContactListOwner): ContactListInteractor =
        ContactListModel(contactDataSource)

    @Provides
    @ContactListScope
    fun provideContactListPresenter(contactListModel: ContactListInteractor): ContactListPresenter =
        ContactListPresenter(contactListModel)
}
