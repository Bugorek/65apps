package com.a65apps.pandaananass.tetsapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.holders.ContactListViewHolder
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import com.a65apps.pandaananass.tetsapplication.util.ContactItemDiffCallback

class ContactListAdapter(private val onContactClick: (ShortContactModel) -> Unit) : ListAdapter<ShortContactModel, ContactListViewHolder>(ContactItemDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return ContactListViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.cell_contact, parent, false)
        ) { position ->
            onContactClick(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}