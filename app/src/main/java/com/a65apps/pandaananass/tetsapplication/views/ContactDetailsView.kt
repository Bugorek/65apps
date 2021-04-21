package com.a65apps.pandaananass.tetsapplication.views

import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactDetailsView: MvpView {
    fun getContactData()
    fun showLoader()
    fun hideLoader()
    fun showRequestError()
    fun setContactData(contactModel: FullContactModel)
    fun noBirthday()
    fun setBirthday(contactModel: FullContactModel)
    fun notificationSet()
    fun notificationNotSet()
    fun setNoPermission()
}