package com.a65apps.pandaananass.tetsapplication.interfaces

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import io.reactivex.rxjava3.core.Single

interface ContactListOwner {
    fun getContactList(context: Context, query: String?): Single<List<ShortContactModel>>
}