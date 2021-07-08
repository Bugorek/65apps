package com.a65apps.pandaananass.tetsapplication.contactRoute

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.domain.contactRoute.DestinationModel
import com.example.domain.contactRoute.SimplePoint
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import javax.inject.Inject
import javax.inject.Provider


const val FRAGMENT_ROUTE_NAME = "ContactRouteFragment"
private const val ARGUMENT_ID = "Id"

class ContactRouteFragment : MvpAppCompatFragment(), ContactRouteView, OnMapReadyCallback,
    OnDestinationClickListener {
    @Inject
    lateinit var contactRoutePresenterProvider: Provider<ContactRoutePresenter>

    @InjectPresenter
    lateinit var contactRoutePresenter: ContactRoutePresenter

    private var contactMap: GoogleMap? = null
    private var mapView: FragmentContainerView? = null
    private var destinationRecycler: RecyclerView? = null
    private var contactRouteAdapter: ContactRouteAdapter? = null
    private var contactRouteDecorator: ContactRouteDecorator? = null
    private var txtEmptyList: TextView? = null
    private var currentContactModel: DestinationModel? = null
    private var destinationContactModel: DestinationModel? = null
    private var progressView: CircularProgressView? = null

    companion object {
        fun getNewInstance(id: String): ContactRouteFragment =
            ContactRouteFragment().apply {
                arguments = bundleOf(
                    ARGUMENT_ID to id
                )
            }
    }

    override fun onAttach(context: Context) {
        val appDelegate = requireActivity().application as? ComponentOwner
        val contactDataComponent = appDelegate?.getAppComponent()
            ?.contactRouteFactory?.create()
        contactDataComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactRouteDecorator = ContactRouteDecorator(
            resources.getDimensionPixelSize(R.dimen.contact_route_list_padding),
            resources.getDimensionPixelSize(R.dimen.contact_route_list_padding)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactroute, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactId = arguments?.getString(ARGUMENT_ID)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.contact_route_map) as? SupportMapFragment
        mapView = view.findViewById(R.id.contact_route_map)
        destinationRecycler = view.findViewById(R.id.recycler_destination)
        txtEmptyList = view.findViewById(R.id.txt_contact_route_empty_list)
        progressView = view.findViewById(R.id.cpv_contact_route)
        contactRouteAdapter = ContactRouteAdapter {
            onDestinationClickListener(it)
        }
        contactRouteDecorator?.let {
            destinationRecycler?.addItemDecoration(it)
        }
        destinationRecycler?.adapter = contactRouteAdapter
        destinationRecycler?.layoutManager = LinearLayoutManager(requireContext())
        destinationRecycler?.setHasFixedSize(true)
        contactId?.let {
            contactRoutePresenter.getContactData(it)
        }
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        mapView = null
        destinationRecycler = null
        contactMap = null
        destinationRecycler = null
        txtEmptyList = null
        contactRouteAdapter = null
        progressView = null
        super.onDestroyView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        contactMap = googleMap
    }

    override fun setDestinationList(
        destinationList: List<DestinationModel>,
        currentContact: DestinationModel
    ) {
        progressView?.visibility = View.GONE
        currentContactModel = currentContact
        contactRouteAdapter?.submitList(destinationList)
    }

    override fun setRoute(points: List<SimplePoint>) {
        val latLngBuilder = LatLngBounds.Builder()
        destinationContactModel?.let {
            latLngBuilder.include(LatLng(it.latitude, it.longitude))
            contactMap?.addMarker(
                MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(it.address)
            )
        }
        currentContactModel?.let {
            latLngBuilder.include(LatLng(it.latitude, it.longitude))
            contactMap?.addMarker(
                MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(it.address)
            )
        }
        points.forEach {
            contactMap?.addPolyline(PolylineOptions().addAll(PolyUtil.decode(it.point)))
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
        progressView?.visibility = View.GONE
        mapView?.visibility = View.VISIBLE
    }

    override fun setRouteDataError() {
        Toast.makeText(
            requireContext(),
            getString(R.string.fragment_contact_route_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun setEmptyList() {
        destinationRecycler?.visibility = View.GONE
        mapView?.visibility = View.GONE
        txtEmptyList?.visibility = View.VISIBLE
    }

    override fun onDestinationClickListener(destination: DestinationModel) {
        destinationRecycler?.visibility = View.GONE
        progressView?.visibility = View.VISIBLE
        destinationContactModel = destination
        currentContactModel?.let {
            contactRoutePresenter.getRouteData(
                origin = it,
                destination = destination
            )
        }
    }

    @ProvidePresenter
    fun providePresenter(): ContactRoutePresenter {
        return contactRoutePresenterProvider.get()
    }
}
