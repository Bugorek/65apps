package com.a65apps.pandaananass.tetsapplication.contact_list

import android.app.Activity
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.a65apps.pandaananass.tetsapplication.common.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.common.PERMISSION_DIALOG_NAME
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.domain.contact_list.ContactListData
import com.example.domain.contact_list.ContactListInteractor
import com.example.domain.contact_list.ShortContactModel
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

    override fun setContactList(contactModel: List<ShortContactModel>) {
        if (contactModel.isEmpty()) {
            viewState.setEmptyList()
        } else {
            viewState.setContactData(contactModel)
        }
    }

    fun getContactData() {
        compositeDisposable.add(
            contactListModel
                .getContactData(query = null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showLoader() }
                .doFinally { viewState.hideLoader() }
                .subscribe({
                    setContactList(contactModel = it)
                }, {
                    viewState.showRequestError()
                })
        )
    }

    fun getContactDataWithQuery(query: String) {
        compositeDisposable.add(
            contactListModel.updateContactData(query = query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showLoader() }
                .doFinally { viewState.hideLoader() }
                .subscribe({
                    setContactList(contactModel = it)
                }, {
                    viewState.showRequestError()
                })
        )
    }

    fun setNoPermission() {
        viewState.setNoPermission()
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
}