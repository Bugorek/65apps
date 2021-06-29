package com.a65apps.pandaananass.tetsapplication.contactDetails

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.domain.contactDetails.FullContactModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactDetailsView : MvpView {
    fun getContactData()
    fun setContactData(contactModel: FullContactModel?)
    fun notificationButtonStyle(notificationStatus: Boolean)
}
