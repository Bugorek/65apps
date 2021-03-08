package com.a65apps.pandaananass.tetsapplication.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.R

private const val ARGUMENT_ID = "Id"

class ContactDetailsFragment: Fragment() {

    companion object {
        fun getNewInstance(id: Int): ContactDetailsFragment {
            val contactDetailsFragment = ContactDetailsFragment()
            val args = Bundle()
            args.putInt(ARGUMENT_ID, id)
            contactDetailsFragment.arguments = args
            return contactDetailsFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        val txtDescription: TextView = view.findViewById(R.id.txt_contact_description)
        txtDescription.movementMethod = ScrollingMovementMethod()
        mainActivity.title = resources.getString(R.string.fragment_contact_details_title)
        txtDescription.text = "$ARGUMENT_ID = ${this.arguments?.get(ARGUMENT_ID).toString()}"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactdetails, null)
    }
}