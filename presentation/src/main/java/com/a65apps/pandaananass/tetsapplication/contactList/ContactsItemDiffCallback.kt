package com.a65apps.pandaananass.tetsapplication.contactList

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.contactList.ShortContactModel

object ContactsItemDiffCallback : DiffUtil.ItemCallback<ShortContactModel>() {
    override fun areItemsTheSame(oldItem: ShortContactModel, newItem: ShortContactModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ShortContactModel,
        newItem: ShortContactModel
    ): Boolean {
        return oldItem.name == newItem.name && oldItem.number == newItem.number
    }
}
