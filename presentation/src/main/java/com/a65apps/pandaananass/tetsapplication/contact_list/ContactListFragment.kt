package com.a65apps.pandaananass.tetsapplication.contact_list

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
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.a65apps.pandaananass.tetsapplication.common.PermissionDialogClickListener
import com.a65apps.pandaananass.tetsapplication.main.OnContactClickListener
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView
import javax.inject.Inject
import javax.inject.Provider

const val FRAGMENT_LIST_NAME = "ContactListFragment"

class ContactListFragment : MvpAppCompatFragment(),
    ContactListView,
    PermissionDialogClickListener {
    @Inject
    lateinit var contactListPresenterProvider: Provider<ContactListPresenter>

    @InjectPresenter
    lateinit var contactListPresenter: ContactListPresenter

    private var contactClickListener: OnContactClickListener? = null
    private var rvContact: RecyclerView? = null
    private var contactsAdapter: ContactsListAdapter? = null
    private var txtNoPermission: TextView? = null
    private var txtEmptyList: TextView? = null
    private var cpvWait: CircularProgressView? = null
    private var txtRequestError: TextView? = null
    private var contactsListDecorator: ContactsListDecorator? = null
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                contactListPresenter.getContactData()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission.READ_CONTACTS
                    )
                ) {
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
        val appDelegate = context.applicationContext as? ComponentOwner
        val contactDataComponent = appDelegate?.getAppComponent()
            ?.contactListFactory?.create()
        contactDataComponent?.inject(contactListFragment = this)
        super.onAttach(context)
        if (context is OnContactClickListener) {
            contactClickListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactsListDecorator = ContactsListDecorator(
            topPaddingDP = resources.getDimensionPixelSize(R.dimen.contact_list_padding),
            sidePaddingDP = resources.getDimensionPixelSize(R.dimen.contact_list_padding)
        )
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
        val mainActivity = activity as? MainActivity
        mainActivity?.title = resources.getString(R.string.fragment_contact_list_title)
        txtNoPermission = view.findViewById(R.id.txt_contact_list_no_permission)
        txtEmptyList = view.findViewById(R.id.txt_contact_list_empty_list)
        rvContact = view.findViewById(R.id.recycler_contact)
        cpvWait = view.findViewById(R.id.cpv_contact_list)
        txtRequestError = view.findViewById(R.id.txt_contact_list_request_error)
        contactsAdapter =
            ContactsListAdapter { contact ->
                contact.id?.let { contactClickListener?.onContactClickListener(it) }
            }
        setRecycler()
        getPermission()
    }

    override fun onDestroyView() {
        txtNoPermission = null
        txtEmptyList = null
        rvContact?.adapter = null
        rvContact = null
        cpvWait = null
        txtRequestError = null
        contactsAdapter = null
        contactsListDecorator = null
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
            .actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { contactListPresenter.getContactDataWithQuery(query = it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { contactListPresenter.getContactDataWithQuery(query = it) }
                return true
            }
        })
    }

    override fun getContactData() {
        contactListPresenter.getContactData()
    }

    override fun setContactData(contactModel: List<com.example.domain.contact_list.ShortContactModel>) {
        txtEmptyList?.visibility = View.GONE
        contactsAdapter?.submitList(contactModel)
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
        contactsAdapter?.submitList(mutableListOf())
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

    @ProvidePresenter
    fun providePresenter(): ContactListPresenter {
        return contactListPresenterProvider.get()
    }

    private fun setRecycler() {
        contactsListDecorator?.let { rvContact?.addItemDecoration(it) }
        rvContact?.adapter = contactsAdapter
        rvContact?.layoutManager = LinearLayoutManager(requireContext())
        rvContact?.setHasFixedSize(true)
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    contactListPresenter.getContactData()
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permission.READ_CONTACTS
                ) -> {
                    contactListPresenter.showPermissionDialog(activity = requireActivity())
                }
                else -> {
                    requestPermission.launch(permission.READ_CONTACTS)
                }
            }
        }
    }
}