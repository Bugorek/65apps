package com.a65apps.pandaananass.tetsapplication.contactList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.a65apps.pandaananass.tetsapplication.R
import com.example.domain.contactList.ShortContactModel

class ContactsListAdapter(private val onContactClick: (ShortContactModel) -> Unit) :
    ListAdapter<ShortContactModel, ContactsListViewHolder>(
        ContactsItemDiffCallback
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsListViewHolder {
        return ContactsListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_contact, parent, false)
        ) { position ->
            onContactClick(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: ContactsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
