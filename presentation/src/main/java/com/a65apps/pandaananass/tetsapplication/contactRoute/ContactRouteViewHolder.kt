package com.a65apps.pandaananass.tetsapplication.contactRoute

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.pandaananass.tetsapplication.R
import com.example.domain.contactRoute.DestinationModel

class ContactRouteViewHolder(itemView: View, onItemClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val txtName = itemView.findViewById<TextView>(R.id.txt_contact_route_name)

    init {
        itemView.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(position)
            }
        }
    }

    fun bind(destination: DestinationModel) {
        txtName?.text = destination.contactName
    }
}
