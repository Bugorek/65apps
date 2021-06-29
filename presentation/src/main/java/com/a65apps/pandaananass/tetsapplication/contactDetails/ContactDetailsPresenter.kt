package com.a65apps.pandaananass.tetsapplication.contactDetails

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.a65apps.pandaananass.tetsapplication.common.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.common.PERMISSION_DIALOG_NAME
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.contactDetails.ContactDetailsData
import com.example.domain.contactDetails.ContactDetailsInteractor
import com.example.domain.contactDetails.FullContactModel
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

    override fun setContactData(contactModel: FullContactModel?) {
        viewState.setContactData(contactModel)
    }

    fun getContactData(id: String) {
        compositeDisposable.add(
            contactDetailsModel
                .getContactData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setContactData(it)
                    },
                    {
                        setContactData(null)
                    }
                )
        )
    }

    fun notificationClick(contactModel: FullContactModel) {
        if (contactDetailsModel.notificationStatus(
                contactModel.id,
                contactModel.name
            ) == false
        ) {
            viewState.notificationButtonStyle(true)
        } else viewState.notificationButtonStyle(false)
        contactDetailsModel.notificationClick(contactModel)
    }

    fun notificationButtonStyle(
        contactId: String?,
        contactName: String
    ) {
        if (contactDetailsModel.notificationStatus(contactId, contactName) == true
        ) {
            viewState.notificationButtonStyle(true)
        } else {
            viewState.notificationButtonStyle(false)
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

    fun getPermission(
        contactId: String?,
        context: Context,
        activity: Activity,
        requestPermission: ActivityResultLauncher<String>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    contactId?.let { getContactData(it) }
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    showPermissionDialog(activity)
                }
                else -> {
                    requestPermission.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }
    }
}
