package com.a65apps.pandaananass.tetsapplication.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.common.AlertDialogFragment
import com.a65apps.pandaananass.tetsapplication.contactDetails.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.contactDetails.FRAGMENT_DETAILS_NAME
import com.a65apps.pandaananass.tetsapplication.contactList.ContactListFragment
import com.a65apps.pandaananass.tetsapplication.contactList.FRAGMENT_LIST_NAME

private const val ARGUMENT_ID = "Id"

class MainActivity :
    AppCompatActivity(),
    OnContactClickListener {

    private var permissionDialogFragment: AlertDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        val contactId = intent.getStringExtra(ARGUMENT_ID)
        if (savedInstanceState == null) {
            openContactList()
        }
        contactId?.let {
            openContactDetails(contactId)
            intent.removeExtra(ARGUMENT_ID)
        }
    }

    override fun onDestroy() {
        permissionDialogFragment = null
        super.onDestroy()
    }

    override fun onContactClickListener(id: String) {
        openContactDetails(id)
    }

    private fun openContactList() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container, ContactListFragment.getNewInstance(),
                FRAGMENT_LIST_NAME
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun openContactDetails(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, ContactDetailsFragment.getNewInstance(id),
                FRAGMENT_DETAILS_NAME
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        supportFragmentManager.executePendingTransactions()
    }
}
