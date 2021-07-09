package com.a65apps.pandaananass.tetsapplication.contactRoute

data class Points(
    val points: String
)

data class Polyline(
    val polyline: Points
)

data class Steps(
    val steps: List<Polyline>
)

data class Legs(
    val legs: List<Steps>
)

data class Routes(
    val routes: List<Legs>
)
