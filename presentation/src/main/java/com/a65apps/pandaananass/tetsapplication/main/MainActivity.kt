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
import com.a65apps.pandaananass.tetsapplication.contactMap.ContactMapFragment
import com.a65apps.pandaananass.tetsapplication.contactMap.FRAGMENT_MAP_NAME
import kotlin.random.Random

private const val ARGUMENT_ID = "Id"

class MainActivity : AppCompatActivity() {

    private var permissionDialogFragment: AlertDialogFragment? = null
    private var fragmentDelegate: FragmentOwner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDelegate = FragmentDelegate(this)
        setContentView(R.layout.activity_main)
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        val contactId = intent.getStringExtra(ARGUMENT_ID)
        if (savedInstanceState == null) {
            fragmentDelegate?.openContactList()
        }
        contactId?.let {
            fragmentDelegate?.openContactDetails(contactId)
            intent.removeExtra(ARGUMENT_ID)
        }
    }

    override fun onDestroy() {
        permissionDialogFragment = null
        fragmentDelegate = null
        super.onDestroy()
    }
}
