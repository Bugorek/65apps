package com.a65apps.pandaananass.tetsapplication.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.fragments.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.fragments.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.fragments.ContactListFragment
import com.a65apps.pandaananass.tetsapplication.fragments.FRAGMENT_LIST_NAME
import com.a65apps.pandaananass.tetsapplication.fragments.PERMISSION_DIALOG_NAME
import com.a65apps.pandaananass.tetsapplication.fragments.FRAGMENT_DETAILS_NAME
import com.a65apps.pandaananass.tetsapplication.interfaces.RelativeLayoutClickListener
import com.a65apps.pandaananass.tetsapplication.interfaces.ServiceOwner
import com.a65apps.pandaananass.tetsapplication.service.ContactService

private const val ARGUMENT_ID = "Id"

class MainActivity : AppCompatActivity(), RelativeLayoutClickListener, ServiceOwner {

    private var contactService: ContactService? = null
    private var permissionDialogFragment: AlertDialogFragment? = null
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder = iBinder as ContactService.SimpleBinder
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

        override fun onServiceDisconnected(componentName: ComponentName?) {
            bound = false
        }
    }
    private val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startService()
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                        showPermissionDialog()
                    } else {
                        emptyContactList()
                    }
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        val contactId = intent.getStringExtra(ARGUMENT_ID)
        if (savedInstanceState == null) {
            openContactList()
        }
        getPermission()
        contactId?.let {
            openContactDetails(contactId)
            intent.removeExtra(ARGUMENT_ID)
        }
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
        }
        contactService = null
        permissionDialogFragment = null
        super.onDestroy()
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startService()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showPermissionDialog()
                }
                else -> {
                    requestPermission.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }
    }

    private fun showPermissionDialog() {
        permissionDialogFragment = if (supportFragmentManager.findFragmentByTag(PERMISSION_DIALOG_NAME) == null) {
            AlertDialogFragment()
        } else {
            supportFragmentManager.findFragmentByTag(PERMISSION_DIALOG_NAME) as AlertDialogFragment
        }
        if (permissionDialogFragment?.isAdded == true) {
            permissionDialogFragment?.dismiss()
        }
        permissionDialogFragment?.show(supportFragmentManager, PERMISSION_DIALOG_NAME)
    }

    fun positiveButtonClick() {
        requestPermission.launch(Manifest.permission.READ_CONTACTS)
    }

    fun negativeButtonClick() {
        emptyContactList()
    }

    private fun emptyContactList() {
        val contactListFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_LIST_NAME) as ContactListFragment
        contactListFragment.emptyListInfo()
    }

    private fun openContactList() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ContactListFragment.getNewInstance(), FRAGMENT_LIST_NAME)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    private fun openContactDetails(id: String) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ContactDetailsFragment.getNewInstance(id), FRAGMENT_DETAILS_NAME)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    override fun onLayoutClick(id: String) {
        openContactDetails(id)
    }

    private fun startService() {
        val intent = Intent(this, ContactService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun getService() = contactService
}