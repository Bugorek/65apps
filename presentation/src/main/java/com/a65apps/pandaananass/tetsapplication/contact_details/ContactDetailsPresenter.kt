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

private const val ARGUMENT_ID = "Id"
private const val ARGUMENT_NAME = "Name"

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
        context: Context,
        contactId: String?,
        contactName: String,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        val intent = getIntent(
            context = context,
            contactId = contactId,
            contactName = contactName
        )
        if (notificationStatus(
                intent = intent,
                id = contactId,
                context = context
            )
        ) {
            deleteNotification(id = contactId, contactName = contactName)
        } else {
            setNotification(
                id = contactId,
                contactName = contactName,
                monthOfBirth = monthOfBirth,
                dayOfBirth = dayOfBirth
            )
        }
    }

    fun notificationButtonStyle(
        context: Context,
        contactId: String?,
        contactName: String
    ) {
        val intent = getIntent(
            context = context,
            contactId = contactId,
            contactName = contactName
        )
        if (notificationStatus(
                intent = intent,
                id = contactId,
                context = context
            )
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

    private fun getIntent(
        context: Context,
        contactId: String?,
        contactName: String
    ) =
        Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ARGUMENT_ID, contactId)
            putExtra(ARGUMENT_NAME, contactName)
            action = context.getString(R.string.notification_action)
        }

    private fun notificationStatus(
        intent: Intent,
        id: String?,
        context: Context
    ): Boolean {
        val status = id?.let {
            PendingIntent.getBroadcast(
                context,
                it.hashCode(),
                intent,
                PendingIntent.FLAG_NO_CREATE
            )
        }
        return status != null
    }

    private fun setNotification(
        id: String?,
        contactName: String,
        monthOfBirth: Int?,
        dayOfBirth: Int?
    ) {
        contactDetailsModel.setNotification(
            id = id,
            contactName = contactName,
            monthOfBirth = monthOfBirth,
            dayOfBirth = dayOfBirth
        )
        viewState.notificationSet()
    }

    private fun deleteNotification(id: String?, contactName: String) {
        contactDetailsModel.deleteNotification(id = id, contactName = contactName)
        viewState.notificationNotSet()
    }
}