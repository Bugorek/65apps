package com.a65apps.pandaananass.tetsapplication.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactServiceInterface
import com.a65apps.pandaananass.tetsapplication.interfaces.RelativeLayoutClickListener
import com.a65apps.pandaananass.tetsapplication.interfaces.ServiceOwner
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import java.lang.ref.WeakReference

const val FRAGMENT_LIST_NAME = "ContactListFragment"

class ContactListFragment: Fragment(), ContactServiceInterface {

    private var listener: RelativeLayoutClickListener? = null
    private var serviceOwner: ServiceOwner? = null
    private var txtName: TextView? = null
    private var txtNumber: TextView? = null
    private var imgAvatar: ImageView? = null
    private var txtEmptyList: TextView? = null
    private var contactCard: RelativeLayout? = null

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
        if (context is ServiceOwner) {
            serviceOwner = context
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
        txtName = view.findViewById(R.id.contact_txt_name)
        txtNumber = view.findViewById(R.id.contact_txt_number)
        imgAvatar = view.findViewById(R.id.contact_img_avatar)
        txtEmptyList = view.findViewById(R.id.txt_contact_list_empty)
        contactCard = view.findViewById(R.id.contact_layout_card)
        serviceOwner?.let {
            getContactData()
        }
    }

    override fun onDestroyView() {
        txtName = null
        txtNumber = null
        imgAvatar = null
        txtEmptyList = null
        contactCard = null
        super.onDestroyView()
    }

    override fun onDetach() {
        serviceOwner = null
        listener = null
        super.onDetach()
    }

    fun setContactList(contactModel: List<ShortContactModel>) {
        activity?.runOnUiThread {
            var contactId: String? = null
            contactModel.forEach {
                contactId = it.id
                txtName?.text = it.name
                txtNumber?.text = it.number
                imgAvatar?.setImageURI(it.photo)
            }
            contactCard?.setOnClickListener {
                contactId?.let { id -> listener?.onLayoutClick(id) }
            }
            contactCard?.visibility = View.VISIBLE
        }
    }

    override fun getContactData() {
        val weakFragment = WeakReference(this)
        serviceOwner?.getService()?.getShortContactData(weakFragment)
    }

    fun emptyListInfo() {
        txtEmptyList?.visibility = View.VISIBLE
    }
}