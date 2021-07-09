package com.a65apps.pandaananass.tetsapplication.contactsLocation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.a65apps.pandaananass.tetsapplication.contactList.ContactListPresenter
import com.a65apps.pandaananass.tetsapplication.contactMap.ContactMapFragment
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.domain.contactMap.SimpleMapData
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject
import javax.inject.Provider

const val FRAGMENT_LOCATION_NAME = "ContactsLocationFragment"

class ContactsLocationFragment : MvpAppCompatFragment(), ContactsLocationView, OnMapReadyCallback {
    @Inject
    lateinit var contactsLocationPresenterProvider: Provider<ContactsLocationPresenter>

    @InjectPresenter
    lateinit var contactsLocationPresenter: ContactsLocationPresenter

    private var mapView: FragmentContainerView? = null
    private var progressView: CircularProgressView? = null
    private var contactMap: GoogleMap? = null
    private var contactEmptyListMessage: TextView? = null

    companion object {
        fun getNewInstance(): ContactsLocationFragment =
            ContactsLocationFragment()
    }

    override fun onAttach(context: Context) {
        val appDelegate = requireActivity().application as? ComponentOwner
        val contactDataComponent = appDelegate?.getAppComponent()
            ?.contactsLocationFactory?.create()
        contactDataComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactslocation, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as? MainActivity
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.contacts_location_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        mainActivity?.title = getString(R.string.fragment_contacts_location_map_title)
        mapView = view.findViewById(R.id.contacts_location_map)
        progressView = view.findViewById(R.id.cpv_contacts_location)
        contactEmptyListMessage = view.findViewById(R.id.txt_contacts_location_empty_list)
    }

    override fun onDestroyView() {
        mapView = null
        progressView = null
        contactMap = null
        contactEmptyListMessage = null
        super.onDestroyView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        contactMap = googleMap
        contactsLocationPresenter.getDestinationContactData()
    }

    override fun setContactsData(contacts: List<SimpleMapData>) {
        progressView?.visibility = View.GONE
        if (contacts.isNotEmpty()) {
            val latLngBuilder = LatLngBounds.Builder()
            contacts.forEach {
                latLngBuilder.include(LatLng(it.latitude, it.longitude))
                contactMap?.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(it.address)
                )
            }
            val size = resources.displayMetrics.widthPixels
            contactMap?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    latLngBuilder.build(),
                    size,
                    size,
                    100
                )
            )
            mapView?.visibility = View.VISIBLE
        }
        else {
            contactEmptyListMessage?.visibility = View.VISIBLE
        }
    }

    override fun setContactListError() {
        Toast.makeText(
            requireContext(),
            getString(R.string.fragment_contact_location_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    @ProvidePresenter
    fun providePresenter(): ContactsLocationPresenter {
        return contactsLocationPresenterProvider.get()
    }
}
