package com.a65apps.pandaananass.tetsapplication.contactMap

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.domain.contactMap.ContactAddress
import com.example.domain.contactMap.SimpleMapData

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactMapView : MvpView {
    fun setContact(contact: SimpleMapData?)
    fun setContactAddress(contactAddress: ContactAddress?, latitude: Double, longitude: Double)
    fun setContactDataStatus(isAdded: Boolean, simpleMapData: SimpleMapData?)
    fun setContactDataError()
}