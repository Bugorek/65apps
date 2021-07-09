package com.a65apps.pandaananass.tetsapplication.contactsLocation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.contactsLocation.ContactsLocationInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class ContactsLocationPresenter(private val contactsLocationModel: ContactsLocationInteractor) :
    MvpPresenter<ContactsLocationView>() {
    fun getDestinationContactData() {
        contactsLocationModel.getDestinationContactData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewState.setContactsData(it)
            }, {
                viewState.setContactListError()
            })
    }
}
