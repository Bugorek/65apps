package com.a65apps.pandaananass.tetsapplication.interfaces

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import io.reactivex.rxjava3.core.Single

interface ContactDetailsOwner {
    fun getContactDetails(context: Context, id: String): Single<FullContactModel>
}