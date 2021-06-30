package com.a65apps.pandaananass.tetsapplication.contactList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.a65apps.pandaananass.tetsapplication.R
import com.example.domain.contactList.ShortContactModel

class ContactsListViewHolder(itemView: View, onItemClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val txtName = itemView.findViewById<TextView>(R.id.contact_txt_name)
    private val txtNumber = itemView.findViewById<TextView>(R.id.contact_txt_number)
    private val imgAvatar = itemView.findViewById<ImageView>(R.id.contact_img_avatar)

    init {
        itemView.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != NO_POSITION) {
                onItemClick(position)
            }
        }
    }

    fun bind(contact: ShortContactModel) {
        txtName?.text = contact.name
        txtNumber?.text = contact.number
        if (contact.photo == null) {
            imgAvatar.setImageResource(R.drawable.ic_account_box_black_24dp)
        } else {
            contact.photo.let {
                imgAvatar?.setImageURI(it?.toUri())
            }
        }
    }
}
