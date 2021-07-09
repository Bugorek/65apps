package com.a65apps.pandaananass.tetsapplication.contactMap

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("results")
    val result: List<Address>
)

data class Address(
    @SerializedName("formatted_address")
    val formattedAddress: String
)
