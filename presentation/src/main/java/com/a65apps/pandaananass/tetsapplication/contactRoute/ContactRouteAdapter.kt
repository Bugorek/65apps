package com.a65apps.pandaananass.tetsapplication.contactRoute

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.a65apps.pandaananass.tetsapplication.R
import com.example.domain.contactRoute.DestinationModel

class ContactRouteAdapter(private val onDestinationClick: (DestinationModel) -> Unit) :
    ListAdapter<DestinationModel, ContactRouteViewHolder>(
        ContactRouteDiffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactRouteViewHolder {
        return ContactRouteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_destination, parent, false)
        ) { position ->
            onDestinationClick(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: ContactRouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}