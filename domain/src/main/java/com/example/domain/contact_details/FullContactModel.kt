package com.example.domain.contact_details

data class FullContactModel(
    var id: String? = null,
    var photo: String? = null,
    var name: String? = null,
    var firstNumber: String = "-",
    var secondNumber: String = "-",
    var firstMail: String = "-",
    var secondMail: String = "-",
    var description: String = "-",
    var monthOfBirth: Int? = null,
    var dayOfBirth: Int? = null
)
