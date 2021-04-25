package com.a65apps.pandaananass.tetsapplication.presenters

import android.app.Activity
import android.content.Context
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.fragments.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.fragments.PERMISSION_DIALOG_NAME
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactListData
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactListOwner
import com.a65apps.pandaananass.tetsapplication.views.ContactListView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel as ShortContactModel

@InjectViewState
class ContactListPresenter @Inject constructor(private val contactDataSource: ContactListOwner): MvpPresenter<ContactListView>(), ContactListData {
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun getContactData(context: Context) {
        compositeDisposable.add(contactDataSource.getContactList(
            context = context,
            query = null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoader() }
            .doFinally { viewState.hideLoader() }
            .subscribe({
                setContactList(contactModel = it)
            }, {
                viewState.showRequestError()
            }))
    }

    fun getContactDataWithQuery(context: Context, query: String) {
        if (query == "") {
            getContactData(context = context)
        } else {
            compositeDisposable.add(contactDataSource.getContactList(
                context = context,
                query = query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showLoader() }
                .doFinally { viewState.hideLoader() }
                .subscribe({
                    setContactList(contactModel = it)
                }, {
                    viewState.showRequestError()
                }))
        }
    }

    override fun setContactList(contactModel: List<ShortContactModel>) {
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