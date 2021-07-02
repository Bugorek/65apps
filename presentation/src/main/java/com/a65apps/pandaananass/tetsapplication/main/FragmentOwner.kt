package com.a65apps.pandaananass.tetsapplication.main

interface FragmentOwner {
    fun openContactList()
    fun openContactDetails(id: String)
    fun openContactMap(id: String?)
}