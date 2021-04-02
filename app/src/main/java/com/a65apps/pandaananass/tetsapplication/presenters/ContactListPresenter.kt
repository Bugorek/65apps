package com.a65apps.pandaananass.tetsapplication.presenters

import android.app.Activity
import android.content.Context
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.data.ContactDataSource
import com.a65apps.pandaananass.tetsapplication.fragments.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.fragments.PERMISSION_DIALOG_NAME
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactListData
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import com.a65apps.pandaananass.tetsapplication.views.ContactListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import java.lang.ref.WeakReference

@InjectViewState
class ContactListPresenter: MvpPresenter<ContactListView>(), ContactListData {
    fun getContactData(context: Context) {
        val weakReference = WeakReference<ContactListData>(this)
        ContactDataSource.getContactList(context = context, contactListData = weakReference)
    }

    override fun setContactData(contactModel: List<ShortContactModel>) {
        if (contactModel.isEmpty()) {
            viewState.setEmptyList()
        } else {
            viewState.setContactData(contactModel)
        }
    }

    fun setNoPermission() {
        viewState.setNoPermission()
    }

    fun showPermissionDialog(activity: Activity){
        val mainActivity = activity as MainActivity
        val permissionDialogFragment = if (mainActivity.supportFragmentManager.findFragmentByTag(PERMISSION_DIALOG_NAME) == null) {
            AlertDialogFragment()
        } else {
            mainActivity.supportFragmentManager.findFragmentByTag(PERMISSION_DIALOG_NAME) as AlertDialogFragment
        }
        if (permissionDialogFragment.isAdded) {
            permissionDialogFragment.dismiss()
        }
        permissionDialogFragment.show(mainActivity.supportFragmentManager, PERMISSION_DIALOG_NAME)
    }
}