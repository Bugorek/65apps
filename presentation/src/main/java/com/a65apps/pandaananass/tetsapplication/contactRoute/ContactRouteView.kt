package com.a65apps.pandaananass.tetsapplication.contactRoute

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.domain.contactRoute.DestinationModel
import com.example.domain.contactRoute.SimplePoint

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ContactRouteView : MvpView {
    fun setDestinationList(
        destinationList: List<DestinationModel>,
        currentContact: DestinationModel
    )
    fun setRoute(points: List<SimplePoint>)
    fun setRouteDataError()
    fun setEmptyList()
}
