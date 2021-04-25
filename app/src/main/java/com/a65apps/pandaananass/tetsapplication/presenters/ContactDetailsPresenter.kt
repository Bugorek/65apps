package com.a65apps.pandaananass.tetsapplication.presenters

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.fragments.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.fragments.PERMISSION_DIALOG_NAME
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactDetailsData
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactDetailsOwner
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.a65apps.pandaananass.tetsapplication.receivers.AlarmReceiver
import com.a65apps.pandaananass.tetsapplication.views.ContactDetailsView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

private const val ARGUMENT_ID = "Id"
private const val ARGUMENT_NAME = "Name"

@InjectViewState
class ContactDetailsPresenter @Inject constructor(private val contactDataSource: ContactDetailsOwner): MvpPresenter<ContactDetailsView>(), ContactDetailsData {
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun getContactData(context: Context, id: String) {
        contactDataSource.getContactDetails(context = context, id = id)
        compositeDisposable.add(contactDataSource.getContactDetails(
            context = context,
            id = id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoader() }
            .doFinally { viewState.hideLoader() }
            .subscribe({
               setContactData(contactModel = it)
            }, {
                viewState.showRequestError()
            }))
    }

    override fun setContactData(contactModel: FullContactModel) {
        viewState.setContactData(contactModel = contactModel)
        if (contactModel.dayOfBirth == null) {
            viewState.noBirthday()
        } else {
            viewState.setBirthday(contactModel = contactModel)
        }
    }

    fun notificationClick(context: Context,
                          contactId: String?,
                          contactName: String,
                          monthOfBirth: Int?,
                          dayOfBirth: Int?) {
        val intent = getIntent(context = context, contactId = contactId, contactName = contactName)
        if (notificationStatus(intent = intent, id = contactId, context = context)) {
            deleteNotification(intent = intent, id = contactId, context = context)
        } else {
            setNotification(intent = intent,
                id = contactId,
                context = context,
                monthOfBirth = monthOfBirth,
                dayOfBirth = dayOfBirth)
        }
    }

    fun notificationButtonStyle(context: Context, contactId: String?, contactName: String) {
        val intent = getIntent(context = context,
            contactId = contactId,
            contactName = contactName)
        if (notificationStatus(intent = intent,
        id = contactId,
        context = context)) {
            viewState.notificationSet()
        } else {
            viewState.notificationNotSet()
        }
    }

    private fun getIntent(context: Context, contactId: String?, contactName: String): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(ARGUMENT_ID, contactId)
        intent.putExtra(ARGUMENT_NAME, contactName)
        intent.action = context.getString(R.string.notification_action)
        return intent
    }

    private fun notificationStatus(intent: Intent, id: String?, context: Context): Boolean {
        val status = id?.let { PendingIntent.getBroadcast(context, it.hashCode(), intent, PendingIntent.FLAG_NO_CREATE) }
        return status != null
    }

    private fun setNotification(intent: Intent, id: String?, context: Context, monthOfBirth: Int?, dayOfBirth: Int?) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = id?.let { it -> PendingIntent.getBroadcast(context, it.hashCode(), intent, 0) }
        if (monthOfBirth != null && dayOfBirth != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, birthdayCalendar(dayOfBirth = dayOfBirth, monthOfBirth = monthOfBirth).timeInMillis, pendingIntent)
        }
        viewState.notificationSet()
    }

    private fun deleteNotification(intent: Intent, id: String?, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = id?.let { it -> PendingIntent.getBroadcast(context, it.hashCode(), intent, 0) }
        alarmManager.cancel(pendingIntent)
        pendingIntent?.cancel()
        viewState.notificationNotSet()
    }

    private fun birthdayCalendar(dayOfBirth: Int, monthOfBirth: Int): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        if (month - monthOfBirth + 1 < 0) {
            calendar.set(Calendar.YEAR, year)
        } else if (month - monthOfBirth + 1 == 0) {
            if (day - dayOfBirth > 0) {
                calendar.set(Calendar.YEAR, year + 1)
            } else {
                calendar.set(Calendar.YEAR, year)
            }
        } else {
            calendar.set(Calendar.YEAR, year + 1)
        }
        calendar.set(Calendar.MONTH, monthOfBirth - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfBirth)
        return calendar
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

    fun setNoPermission() {
        viewState.setNoPermission()
    }
}