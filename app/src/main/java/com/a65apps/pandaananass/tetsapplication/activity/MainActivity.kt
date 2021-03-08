package com.a65apps.pandaananass.tetsapplication.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.RelativeLayoutClickListener
import com.a65apps.pandaananass.tetsapplication.fragments.ContactDetailsFragment
import com.a65apps.pandaananass.tetsapplication.fragments.ContactListFragment

class MainActivity : AppCompatActivity(), RelativeLayoutClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val contactListFragment = ContactListFragment.getNewInstance()
            fragmentTransaction
                    .add(R.id.fragment_container, contactListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
        }
    }

    fun openContactDetails(id: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val contactDetailsFragment = ContactDetailsFragment.getNewInstance(id)
        fragmentTransaction
                .replace(R.id.fragment_container, contactDetailsFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    override fun onLayoutClick(id: Int) {
        openContactDetails(id)
    }
}