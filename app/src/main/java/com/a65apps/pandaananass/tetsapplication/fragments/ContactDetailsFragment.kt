package com.a65apps.pandaananass.tetsapplication.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactServiceInterface
import com.a65apps.pandaananass.tetsapplication.interfaces.ServiceOwner
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.a65apps.pandaananass.tetsapplication.service.ContactService
import java.lang.ref.WeakReference

private const val ARGUMENT_ID = "Id"
const val FRAGMENT_DETAILS_NAME = "ContactDetailsFragment"

class ContactDetailsFragment: Fragment(), ContactServiceInterface {

    private var serviceOwner: ServiceOwner? = null
    private var rlContact: RelativeLayout? = null
    private var txtName: TextView? = null
    private var txtFirstNumber: TextView? = null
    private var txtFirstMail: TextView? = null
    private var txtSecondNumber: TextView? = null
    private var txtSecondMail: TextView? = null
    private var txtDescription: TextView? = null

    companion object {
        fun getNewInstance(id: Int): ContactDetailsFragment {
            val contactDetailsFragment = ContactDetailsFragment()
            val args = Bundle()
            args.putInt(ARGUMENT_ID, id)
            contactDetailsFragment.arguments = args
            return contactDetailsFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ServiceOwner) {
            serviceOwner = context
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactdetails, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        rlContact = view.findViewById(R.id.rl_contact_details)
        txtName = view.findViewById(R.id.txt_contact_name)
        txtFirstNumber = view.findViewById(R.id.txt_contact_first_number)
        txtFirstMail = view.findViewById(R.id.txt_contact_first_mail)
        txtSecondNumber = view.findViewById(R.id.txt_contact_second_number)
        txtSecondMail = view.findViewById(R.id.txt_contact_second_mail)
        txtDescription = view.findViewById(R.id.txt_contact_description)
        txtDescription?.movementMethod = ScrollingMovementMethod()
        mainActivity.title = resources.getString(R.string.fragment_contact_details_title)
        if (savedInstanceState == null) {
            this.getContactData()
        }
    }

    override fun onDestroyView() {
        txtName = null
        txtFirstNumber = null
        txtFirstMail = null
        txtSecondNumber = null
        txtSecondMail = null
        txtDescription = null
        rlContact = null
        super.onDestroyView()
    }

    override fun onDetach() {
        serviceOwner = null
        super.onDetach()
    }

    fun setContactDetails(contactModel: FullContactModel) {
        activity?.runOnUiThread {
            txtName?.text = contactModel.name
            txtFirstNumber?.text = contactModel.firstNumber
            txtFirstMail?.text = contactModel.firstMail
            txtSecondNumber?.text = contactModel.secondNumber
            txtSecondMail?.text = contactModel.secondMail
            txtDescription?.text = contactModel.description
            rlContact?.visibility = View.VISIBLE
        }
    }

    override fun getContactData() {
        val weakFragment = WeakReference(this)
        this.arguments?.getInt(ARGUMENT_ID)?.let {
            serviceOwner?.getService()?.getFullContactData(weakFragment, it)
        }
    }
}