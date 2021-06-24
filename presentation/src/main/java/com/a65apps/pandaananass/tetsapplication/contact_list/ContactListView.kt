package com.a65apps.pandaananass.tetsapplication.contact_list

import com.example.domain.contact_list.ShortContactModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactListView : MvpView {
    fun getContactData()
    fun setContactData(contactModel: List<ShortContactModel>)
    fun showLoader()
    fun hideLoader()
    fun showRequestError()
    fun setEmptyList()
    fun setNoPermission()
}