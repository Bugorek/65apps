package com.a65apps.pandaananass.tetsapplication.api

import android.content.Context

interface AppContainer {
    val contactListFactory: ContactListContainer.Factory

    val contactDetailsFactory: ContactDetailsContainer.Factory

    interface Factory {
        fun create(context: Context): AppContainer
    }
}