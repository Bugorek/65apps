package com.a65apps.pandaananass.tetsapplication.contactList

import android.Manifest.permission
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.a65apps.pandaananass.tetsapplication.common.PermissionDialogClickListener
import com.a65apps.pandaananass.tetsapplication.main.FragmentDelegate
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.a65apps.pandaananass.tetsapplication.main.OnContactClickListener
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.domain.contactList.ShortContactModel
import com.github.rahatarmanahmed.cpv.CircularProgressView
import javax.inject.Inject
import javax.inject.Provider

const val FRAGMENT_LIST_NAME = "ContactListFragment"

class ContactListFragment :
    MvpAppCompatFragment(),
    ContactListView,
    PermissionDialogClickListener {
    @Inject
    lateinit var contactListPresenterProvider: Provider<ContactListPresenter>

    @InjectPresenter
    lateinit var contactListPresenter: ContactListPresenter

    private var contactClickListener: OnContactClickListener? = null
    private var contactsRecycler: RecyclerView? = null
    private var contactsAdapter: ContactsListAdapter? = null
    private var txtNoPermission: TextView? = null
    private var txtEmptyList: TextView? = null
    private var progressView: CircularProgressView? = null
    private var txtRequestError: TextView? = null
    private var contactsListDecorator: ContactsListDecorator? = null
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                progressView?.visibility = View.VISIBLE
                contactListPresenter.getContactData()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission.READ_CONTACTS
                    )
                ) {
                    contactListPresenter.showPermissionDialog(activity = requireActivity())
                } else {
                    txtNoPermission?.visibility = View.VISIBLE
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.let {
            contactClickListener = FragmentDelegate(it)
        }
        contactsListDecorator = ContactsListDecorator(
            resources.getDimensionPixelSize(R.dimen.contact_list_padding),
            resources.getDimensionPixelSize(R.dimen.contact_list_padding)
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
        contactsRecycler = view.findViewById(R.id.recycler_contact)
        progressView = view.findViewById(R.id.cpv_contact_list)
        txtRequestError = view.findViewById(R.id.txt_contact_list_request_error)
        contactsAdapter =
            ContactsListAdapter { contact ->
                contact.id?.let { contactClickListener?.onContactClickListener(it) }
            }
        contactsListDecorator?.let {
            contactsRecycler?.addItemDecoration(it)
        }
        contactsRecycler?.adapter = contactsAdapter
        contactsRecycler?.layoutManager = LinearLayoutManager(requireContext())
        contactsRecycler?.setHasFixedSize(true)
        contactListPresenter.getPermission(
            context = requireContext(),
            activity = requireActivity(),
            requestPermission = requestPermission
        )
    }

    override fun onDestroyView() {
        txtNoPermission = null
        txtEmptyList = null
        contactsRecycler?.adapter = null
        contactsRecycler = null
        progressView = null
        txtRequestError = null
        contactsAdapter = null
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
                query?.let {
                    contactListPresenter.getContactDataWithQuery(it)
                    progressView?.visibility = View.VISIBLE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    contactListPresenter.getContactDataWithQuery(it)
                    progressView?.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    override fun setContactData(contactModel: List<ShortContactModel>?) {
        txtRequestError?.isVisible = contactModel == null
        txtEmptyList?.isVisible = contactModel?.isEmpty() == true
        contactsAdapter?.submitList(contactModel)
        progressView?.visibility = View.GONE
    }

    override fun buttonClickStatus(clickStatus: Boolean) {
        if (clickStatus) {
            requestPermission.launch(permission.READ_CONTACTS)
        } else {
            txtNoPermission?.visibility = View.VISIBLE
        }
    }

    @ProvidePresenter
    fun providePresenter(): ContactListPresenter {
        return contactListPresenterProvider.get()
    }
}
