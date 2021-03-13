package com.a65apps.pandaananass.tetsapplication.interfaces

import com.a65apps.pandaananass.tetsapplication.service.ContactService

interface ServiceOwner {
    fun getService(): ContactService?
}