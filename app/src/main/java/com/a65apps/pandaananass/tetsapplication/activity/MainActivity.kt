package com.a65apps.pandaananass.tetsapplication.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.FragmentTransaction
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.interfaces.RelativeLayoutClickListener
import com.a65apps.pandaananass.tetsapplication.fragments.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.fragments.ContactListFragment
import com.a65apps.pandaananass.tetsapplication.fragments.FRAGMENT_DETAILS_NAME
import com.a65apps.pandaananass.tetsapplication.fragments.FRAGMENT_LIST_NAME
import com.a65apps.pandaananass.tetsapplication.interfaces.ServiceOwner
import com.a65apps.pandaananass.tetsapplication.service.ContactService

class MainActivity : AppCompatActivity(), RelativeLayoutClickListener, ServiceOwner {

    private var contactService: ContactService? = null
    private var bound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as ContactService.SimpleBinder
            val contactDetailsFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_DETAILS_NAME)
            val contactListFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_LIST_NAME)
            bound = true
            contactService = binder.getService()
            if (contactDetailsFragment != null) {
                (contactDetailsFragment as ContactDetailsFragment)
                        .getContactData()
            } else if (contactListFragment != null) {
                (contactListFragment as ContactListFragment)
                        .getContactData()
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            bound = false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        val intent = Intent(this, ContactService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        if (savedInstanceState == null) {
            openContactList()
        }
    }

    private fun openContactList() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ContactListFragment.getNewInstance(), FRAGMENT_LIST_NAME)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    private fun openContactDetails(id: Int) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ContactDetailsFragment.getNewInstance(id), FRAGMENT_DETAILS_NAME)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
        }
        contactService = null
        super.onDestroy()
    }

    override fun onLayoutClick(id: Int) {
        openContactDetails(id)
    }

    override fun getService() = contactService
}