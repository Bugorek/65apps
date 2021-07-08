package com.a65apps.pandaananass.tetsapplication.contactRoute

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.contactRoute.DestinationModel

object ContactRouteDiffCallback : DiffUtil.ItemCallback<DestinationModel>() {
    override fun areItemsTheSame(oldItem: DestinationModel, newItem: DestinationModel): Boolean {
        return oldItem.contactName == newItem.contactName
    }

    override fun areContentsTheSame(
        oldItem: DestinationModel,
        newItem: DestinationModel
    ): Boolean {
        return oldItem.address == newItem.address &&
                oldItem.latitude == newItem.latitude &&
                oldItem.longitude == newItem.longitude
    }
}
