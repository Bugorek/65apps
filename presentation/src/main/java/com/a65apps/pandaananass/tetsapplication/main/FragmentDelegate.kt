package com.a65apps.pandaananass.tetsapplication.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.contactDetails.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.contactDetails.FRAGMENT_DETAILS_NAME
import com.a65apps.pandaananass.tetsapplication.contactList.ContactListFragment
import com.a65apps.pandaananass.tetsapplication.contactList.FRAGMENT_LIST_NAME
import com.a65apps.pandaananass.tetsapplication.contactMap.ContactMapFragment
import com.a65apps.pandaananass.tetsapplication.contactMap.FRAGMENT_MAP_NAME
import com.a65apps.pandaananass.tetsapplication.contactRoute.ContactRouteFragment
import com.a65apps.pandaananass.tetsapplication.contactRoute.FRAGMENT_ROUTE_NAME
import com.a65apps.pandaananass.tetsapplication.contactsLocation.ContactsLocationFragment
import com.a65apps.pandaananass.tetsapplication.contactsLocation.FRAGMENT_LOCATION_NAME

class FragmentDelegate(private val activity: AppCompatActivity) : FragmentOwner,
    OnContactClickListener {
    override fun openContactList() {
        activity.supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container, ContactListFragment.getNewInstance(),
                FRAGMENT_LIST_NAME
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    override fun openContactDetails(id: String) {
        activity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, ContactDetailsFragment.getNewInstance(id),
                FRAGMENT_DETAILS_NAME
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    override fun openContactMap(id: String?) {
        activity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, ContactMapFragment.getNewInstance(id),
                FRAGMENT_MAP_NAME
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    override fun openContactRoute(id: String) {
        activity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, ContactRouteFragment.getNewInstance(id),
                FRAGMENT_ROUTE_NAME
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    override fun openContactsLocation() {
        activity.supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, ContactsLocationFragment.getNewInstance(),
                FRAGMENT_LOCATION_NAME
            )
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        activity.supportFragmentManager.executePendingTransactions()
    }

    override fun onContactClickListener(id: String) {
        openContactDetails(id)
    }
}
