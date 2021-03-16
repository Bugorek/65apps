package com.a65apps.pandaananass.tetsapplication.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.a65apps.pandaananass.tetsapplication.fragments.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.fragments.ContactListFragment
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import java.lang.ref.WeakReference

class ContactService : Service() {

    private val binder = SimpleBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun getFullContactData(fragment: WeakReference<ContactDetailsFragment>, id: Int) {
        Thread{
            kotlin.run {
                Thread.sleep(2000)
                val contactList: List<FullContactModel> = listOf(
                        FullContactModel(id = id, name = "Igor Kamashev",
                                firstNumber = "89991892440", secondNumber = "89991892441",
                                firstMail = "1@gmail.com", secondMail = "2@gmail.com",
                                description = "Description"))
                contactList.forEach {
                    if (it.id == id) {
                        fragment.get()?.setContactDetails(it)
                    }
                }
            }
        }.start()
    }

    fun getShortContactData(fragment: WeakReference<ContactListFragment>) {
        Thread{
            kotlin.run {
                Thread.sleep(2000)
                val contactList: List<ShortContactModel> = listOf(ShortContactModel(id = 1,
                        name = "Igor Kamashev",
                        number = "89991892440"))
                fragment.get()?.setContactList(contactModel = contactList)
            }
        }.start()
    }

    inner class SimpleBinder: Binder() {
        fun getService(): ContactService = this@ContactService
    }
}