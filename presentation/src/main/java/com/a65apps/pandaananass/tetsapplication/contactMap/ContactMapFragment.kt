package com.a65apps.pandaananass.tetsapplication.contactMap

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.a65apps.pandaananass.tetsapplication.main.FragmentOwner
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.domain.contactMap.ContactAddress
import com.example.domain.contactMap.SimpleMapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider

const val FRAGMENT_MAP_NAME = "ContactMapFragment"
private const val ARGUMENT_ID = "Id"

class ContactMapFragment : MvpAppCompatFragment(), ContactMapView, OnMapReadyCallback {
    @Inject
    lateinit var contactMapPresenterProvider: Provider<ContactMapPresenter>

    @InjectPresenter
    lateinit var contactMapPresenter: ContactMapPresenter

    private var locationText: TextView? = null
    private var contactMap: GoogleMap? = null
    private var btnSaveDirection: Button? = null
    private var mapDataEntity: SimpleMapData? = null

    companion object {
        fun getNewInstance(id: String?): ContactMapFragment =
            ContactMapFragment().apply {
                arguments = bundleOf(
                    ARGUMENT_ID to id
                )
            }
    }

    override fun onAttach(context: Context) {
        val appDelegate = requireActivity().application as? ComponentOwner
        val contactDataComponent = appDelegate?.getAppComponent()
            ?.contactMapFactory?.create()
        contactDataComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactmap, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as? MainActivity
        val contactId = arguments?.getString(ARGUMENT_ID)
        mainActivity?.title = getString(R.string.fragment_contact_map_title)
        locationText = view.findViewById(R.id.txt_map_direction_text)
        btnSaveDirection = view.findViewById(R.id.btn_contact_map_save)
        contactId?.let {
            contactMapPresenter.getContact(it)
        }
    }

    override fun onDestroyView() {
        locationText = null
        btnSaveDirection = null
        mapDataEntity = null
        contactMap = null
        super.onDestroyView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        contactMap = googleMap
        contactMap?.setOnMapClickListener {
            contactMapPresenter.getContactAddress(it.latitude, it.longitude)
        }
        mapDataEntity?.let {
            setMarker(
                contactMap = contactMap,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }

    override fun setContact(contact: SimpleMapData?) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.contact_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        contact?.let {
            mapDataEntity = contact
            locationText?.text = contact.address
        }
        setSaveButtonModel(contact)
    }

    override fun setContactAddress(
        contactAddress: ContactAddress?,
        latitude: Double,
        longitude: Double
    ) {
        val contactId = arguments?.getString(ARGUMENT_ID)
        contactAddress?.address?.let { address ->
            contactId?.let { id ->
                mapDataEntity = SimpleMapData(
                    id = id,
                    address = address,
                    latitude = latitude,
                    longitude = longitude
                )
            }
            setMarker(
                contactMap = contactMap,
                latitude = latitude,
                longitude = longitude
            )
            locationText?.text = address
        }
        if (contactAddress == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.fragment_contact_map_location_query_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun setContactDataStatus(isAdded: Boolean, simpleMapData: SimpleMapData?) {
        if (isAdded) {
            Toast.makeText(
                requireContext(),
                getString(R.string.fragment_contact_map_update_btn),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(), getString(R.string.fragment_contact_map_add_btn),
                Toast.LENGTH_SHORT
            ).show()
            setSaveButtonModel(simpleMapData)
        }
    }

    override fun setContactDataError() {
        Toast.makeText(
            requireContext(), getString(R.string.fragment_contact_map_data_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    @ProvidePresenter
    fun providePresenter(): ContactMapPresenter {
        return contactMapPresenterProvider.get()
    }

    private fun setSaveButtonModel(simpleMapData: SimpleMapData?) {
        if (simpleMapData != null) {
            btnSaveDirection?.setOnClickListener {
                mapDataEntity?.let { data ->
                    contactMapPresenter.updateContactData(data)
                }
            }
        } else {
            btnSaveDirection?.setOnClickListener {
                mapDataEntity?.let { data ->
                    contactMapPresenter.saveContactData(data)
                }
            }
        }
    }

    private fun setMarker(contactMap: GoogleMap?, latitude: Double, longitude: Double) {
        contactMap?.clear()
        contactMap?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)));
        contactMap?.addMarker(MarkerOptions().position(LatLng(latitude, longitude)))
    }
}