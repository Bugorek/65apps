package com.example.application.contact_list

import com.example.domain.contact_list.ContactListModel
import com.a65apps.pandaananass.tetsapplication.contact_list.ContactListPresenter
import com.example.domain.contact_list.ContactListInteractor
import com.example.domain.contact_list.ContactListOwner
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {
    @Provides
    @ContactListScope
    fun provideContactListInteractor(contactDataSource: ContactListOwner): ContactListInteractor =
            ContactListModel(contactDataSource = contactDataSource)

    @Provides
    @ContactListScope
    fun provideContactListPresenter(contactListModel: ContactListInteractor): ContactListPresenter =
            ContactListPresenter(contactListModel = contactListModel)
}