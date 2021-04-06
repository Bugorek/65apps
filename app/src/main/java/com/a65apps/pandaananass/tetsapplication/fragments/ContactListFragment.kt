package com.a65apps.pandaananass.tetsapplication.fragments

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.interfaces.PermissionDialogClickListener
import com.a65apps.pandaananass.tetsapplication.interfaces.RelativeLayoutClickListener
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import com.a65apps.pandaananass.tetsapplication.presenters.ContactListPresenter
import com.a65apps.pandaananass.tetsapplication.views.ContactListView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

const val FRAGMENT_LIST_NAME = "ContactListFragment"

class ContactListFragment: MvpAppCompatFragment(), ContactListView, PermissionDialogClickListener {

    @InjectPresenter
    lateinit var contactListPresenter: ContactListPresenter

    private var listener: RelativeLayoutClickListener? = null
    private var txtName: TextView? = null
    private var txtNumber: TextView? = null
    private var txtNoPermission: TextView? = null
    private var txtEmptyList: TextView? = null
    private var imgAvatar: ImageView? = null
    private var contactCard: RelativeLayout? = null
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            contactListPresenter.getContactData(context = requireContext())
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission.READ_CONTACTS)) {
                contactListPresenter.showPermissionDialog(activity = requireActivity())
            } else {
                contactListPresenter.setNoPermission()
            }
        }
    }

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
        txtName = view.findViewById(R.id.contact_txt_name)
        txtNumber = view.findViewById(R.id.contact_txt_number)
        txtNoPermission = view.findViewById(R.id.txt_contact_list_no_permission)
        txtEmptyList = view.findViewById(R.id.txt_contact_list_empty_list)
        imgAvatar = view.findViewById(R.id.contact_img_avatar)
        contactCard = view.findViewById(R.id.contact_layout_card)
        getPermission()
    }

    override fun onDestroyView() {
        txtName = null
        txtNumber = null
        txtNoPermission = null
        txtEmptyList = null
        imgAvatar = null
        contactCard = null
        super.onDestroyView()
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun getContactData() {
        contactListPresenter.getContactData(requireContext())
    }

    override fun setContactData(contactModel: List<ShortContactModel>) {
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

    override fun setEmptyList() {
        txtEmptyList?.visibility = View.VISIBLE
    }

    override fun setNoPermission() {
        txtNoPermission?.visibility = View.VISIBLE
    }

    override fun negativeClick() {
        contactListPresenter.setNoPermission()
    }

    override fun positiveClick() {
        requestPermission.launch(permission.READ_CONTACTS)
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    contactListPresenter.getContactData(context = requireContext())
                }
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission.READ_CONTACTS) -> {
                    contactListPresenter.showPermissionDialog(activity = requireActivity())
                }
                else -> {
                    requestPermission.launch(permission.READ_CONTACTS)
                }
            }
        }
    }
}