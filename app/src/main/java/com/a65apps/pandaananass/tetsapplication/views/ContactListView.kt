package com.a65apps.pandaananass.tetsapplication.views

import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactListView: MvpView {
    fun getContactData()
    fun setContactData(contactModel: List<ShortContactModel>)
    fun showLoader()
    fun hideLoader()
    fun showRequestError()
    fun setEmptyList()
    fun setNoPermission()
}