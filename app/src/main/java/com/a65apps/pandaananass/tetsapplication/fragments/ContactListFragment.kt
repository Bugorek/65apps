package com.a65apps.pandaananass.tetsapplication.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.RelativeLayoutClickListener

class ContactListFragment: Fragment() {

    private val testId = 2
    private var listener: RelativeLayoutClickListener? = null

    companion object {
        fun getNewInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RelativeLayoutClickListener) {
            listener = context
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactlist, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.title = resources.getString(R.string.fragment_contact_list_title)
        val contactCard: RelativeLayout = view.findViewById(R.id.contact_layout_card)
        contactCard.setOnClickListener {
            listener?.onLayoutClick(testId)
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }
}