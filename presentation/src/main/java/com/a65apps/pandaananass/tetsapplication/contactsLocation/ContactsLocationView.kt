package com.a65apps.pandaananass.tetsapplication.contactsLocation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.domain.contactMap.SimpleMapData

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactsLocationView : MvpView {
    fun setContactsData(contacts: List<SimpleMapData>)
    fun setContactListError()
}
