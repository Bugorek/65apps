package com.a65apps.pandaananass.tetsapplication.contactList

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
import com.example.domain.contactList.ContactListData
import com.example.domain.contactList.ContactListInteractor
import com.example.domain.contactList.ShortContactModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

@InjectViewState
class ContactListPresenter(private val contactListModel: ContactListInteractor) :
    MvpPresenter<ContactListView>(),
    ContactListData {
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun setContactList(contactModel: List<ShortContactModel>?) {
        viewState.setContactData(contactModel)
    }

    fun getContactData() {
        compositeDisposable.add(
            contactListModel
                .getContactData(query = null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setContactList(it)
                    },
                    {
                        setContactList(null)
                    }
                )
        )
    }

    fun getContactDataWithQuery(query: String) {
        compositeDisposable.add(
            contactListModel.updateContactData(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        setContactList(it)
                    },
                    {
                        setContactList(null)
                    }
                )
        )
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
                it.dismiss()
            }
        }
        mainActivity?.let {
            permissionDialogFragment?.show(it.supportFragmentManager, PERMISSION_DIALOG_NAME)
        }
    }

    fun getPermission(
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
                    getContactData()
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
