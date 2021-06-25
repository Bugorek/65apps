package com.a65apps.pandaananass.tetsapplication.contact_details

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.common.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.common.PERMISSION_DIALOG_NAME
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.example.domain.contact_details.ContactDetailsData
import com.example.domain.contact_details.FullContactModel
import com.a65apps.pandaananass.tetsapplication.receivers.AlarmReceiver
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.contact_details.ContactDetailsInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class ContactDetailsPresenter(private val contactDetailsModel: ContactDetailsInteractor) :
    MvpPresenter<ContactDetailsView>(),
    ContactDetailsData {
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun setContactData(contactModel: FullContactModel) {
        viewState.setContactData(contactModel = contactModel)
        if (contactModel.dayOfBirth == null) {
            viewState.noBirthday()
        } else {
            viewState.setBirthday(contactModel = contactModel)
        }
    }

    fun getContactData(id: String) {
        compositeDisposable.add(
            contactDetailsModel
                .getContactData(id = id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showLoader() }
                .doFinally { viewState.hideLoader() }
                .subscribe({
                    setContactData(contactModel = it)
                }, {
                    viewState.showRequestError()
                })
        )
    }

    fun notificationClick(
        contactId: String?,
        contactName: String?,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        if(contactDetailsModel.notificationStatus(id = contactId, contactName = contactName) == false) {
            viewState.notificationSet()
        } else viewState.notificationNotSet()
        contactDetailsModel.notificationClick(id = contactId, contactName = contactName, monthOfBirth = monthOfBirth, dayOfBirth = dayOfBirth)
    }

    fun notificationButtonStyle(
        contactId: String?,
        contactName: String
    ) {
        if (contactDetailsModel.notificationStatus(id = contactId, contactName = contactName) == true
        ) {
            viewState.notificationSet()
        } else {
            viewState.notificationNotSet()
        }
    }

    fun showPermissionDialog(activity: Activity) {
        val mainActivity = activity as? MainActivity
        val permissionDialogFragment = if (mainActivity?.supportFragmentManager?.findFragmentByTag(
                PERMISSION_DIALOG_NAME
            ) == null
        ) {
            AlertDialogFragment()
        } else {
            mainActivity.supportFragmentManager.findFragmentByTag(PERMISSION_DIALOG_NAME) as? AlertDialogFragment
        }
        permissionDialogFragment?.let {
            if (it.isAdded) {
                permissionDialogFragment.dismiss()
            }

        }
        mainActivity?.let {
            permissionDialogFragment?.show(
                it.supportFragmentManager,
                PERMISSION_DIALOG_NAME
            )
        }
    }

    fun setNoPermission() {
        viewState.setNoPermission()
    }
}