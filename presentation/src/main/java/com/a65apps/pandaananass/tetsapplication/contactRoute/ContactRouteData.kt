package com.a65apps.pandaananass.tetsapplication.contactRoute

import android.content.Context
import com.a65apps.pandaananass.tetsapplication.R
import com.example.domain.contactRoute.ContactRouteOwner
import com.example.domain.contactRoute.DestinationModel
import com.example.domain.contactRoute.SimplePoint
import io.reactivex.rxjava3.core.Observable

class ContactRouteData(private val context: Context, private val directionApi: DirectionApi) :
    ContactRouteOwner {
    override fun getContactRoute(
        origin: DestinationModel,
        destination: DestinationModel
    ): Observable<List<SimplePoint>> =
        directionApi.getAddress(
            context.resources.getString(R.string.google_maps_key),
            "${origin.latitude}, ${origin.longitude}",
            "${destination.latitude}, ${destination.longitude}"
        ).map { convertToSimplePoints(it) }

    private fun convertToSimplePoints(routes: Routes): List<SimplePoint> {
        val simplePoints: MutableList<SimplePoint> = mutableListOf()
        for (rout in routes.routes) {
            for (leg in rout.legs) {
                for (step in leg.steps) {
                    simplePoints.add(SimplePoint(step.polyline.points))
                }
            }
        }
        return simplePoints
    }
}
