package com.a65apps.pandaananass.tetsapplication.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.a65apps.pandaananass.tetsapplication.data.ContactDataSource
import com.a65apps.pandaananass.tetsapplication.fragments.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.fragments.ContactListFragment
import java.lang.ref.WeakReference

class ContactService : Service() {

    private val binder = SimpleBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun getFullContactData(fragment: WeakReference<ContactDetailsFragment>, id: String) {
        Thread {
            kotlin.run {
                Thread.sleep(2000)
                fragment.get()?.setContactDetails(ContactDataSource.getContactDetails(context = applicationContext, id = id))
            }
        }.start()
    }

    fun getShortContactData(fragment: WeakReference<ContactListFragment>) {
        Thread {
            kotlin.run {
                Thread.sleep(2000)
                fragment.get()?.setContactList(ContactDataSource.getContactList(context = applicationContext))
            }
        }.start()
    }

    inner class SimpleBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }
}