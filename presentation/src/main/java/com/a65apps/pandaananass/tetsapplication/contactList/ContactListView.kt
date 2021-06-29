package com.a65apps.pandaananass.tetsapplication.contactList

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.domain.contactList.ShortContactModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactListView : MvpView {
    fun setContactData(contactModel: List<ShortContactModel>?)
}
