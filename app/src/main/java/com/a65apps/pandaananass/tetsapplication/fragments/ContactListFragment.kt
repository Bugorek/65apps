package com.a65apps.pandaananass.tetsapplication.fragments

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.Menu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.adapters.ContactListAdapter
import com.a65apps.pandaananass.tetsapplication.interfaces.PermissionDialogClickListener
import com.a65apps.pandaananass.tetsapplication.interfaces.OnContactClickListener
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import com.a65apps.pandaananass.tetsapplication.presenters.ContactListPresenter
import com.a65apps.pandaananass.tetsapplication.util.ContactListDecorator
import com.a65apps.pandaananass.tetsapplication.views.ContactListView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView

const val FRAGMENT_LIST_NAME = "ContactListFragment"

class ContactListFragment: MvpAppCompatFragment(), ContactListView, PermissionDialogClickListener {

    @InjectPresenter
    lateinit var contactListPresenter: ContactListPresenter

    private var contactClickListener: OnContactClickListener? = null
    private var rvContact: RecyclerView? = null
    private var contactAdapter: ContactListAdapter? = null
    private var txtNoPermission: TextView? = null
    private var txtEmptyList: TextView? = null
    private var cpvWait: CircularProgressView? = null
    private var txtRequestError: TextView? = null
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
        if (context is OnContactClickListener) {
            contactClickListener = context
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_contactlist, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        mainActivity.title = resources.getString(R.string.fragment_contact_list_title)
        txtNoPermission = view.findViewById(R.id.txt_contact_list_no_permission)
        txtEmptyList = view.findViewById(R.id.txt_contact_list_empty_list)
        rvContact = view.findViewById(R.id.recycler_contact)
        cpvWait = view.findViewById(R.id.cpv_contact_list)
        txtRequestError = view.findViewById(R.id.txt_contact_list_request_error)
        contactAdapter = ContactListAdapter { contact ->
            contact.id?.let { contactClickListener?.onContactClickListener(it) }
        }
        rvContact?.addItemDecoration(ContactListDecorator(
                topPaddingDP = resources.getDimensionPixelSize(R.dimen.contact_list_padding),
                sidePaddingDP = resources.getDimensionPixelSize(R.dimen.contact_list_padding)))
        rvContact?.adapter = contactAdapter
        rvContact?.layoutManager = LinearLayoutManager(requireContext())
        rvContact?.setHasFixedSize(true)
        getPermission()
    }

    override fun onDestroyView() {
        txtNoPermission = null
        txtEmptyList = null
        rvContact?.adapter = null
        rvContact = null
        contactAdapter = null
        cpvWait = null
        txtRequestError = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactClickListener = null
        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.action_search)
                .actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { contactListPresenter.getContactDataWithQuery(context = requireContext(), query = it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { contactListPresenter.getContactDataWithQuery(context = requireContext(), query = it) }
                return true
            }
        })
    }

    override fun getContactData() {
        contactListPresenter.getContactData(requireContext())
    }

    override fun setContactData(contactModel: List<ShortContactModel>) {
        txtEmptyList?.visibility = View.GONE
        contactAdapter?.submitList(contactModel)
    }

    override fun showLoader() {
        cpvWait?.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        cpvWait?.visibility = View.GONE
    }

    override fun showRequestError() {
        txtRequestError?.visibility = View.VISIBLE
    }

    override fun setEmptyList() {
        contactAdapter?.submitList(mutableListOf())
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