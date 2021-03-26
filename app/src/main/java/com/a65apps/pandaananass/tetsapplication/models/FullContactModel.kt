package com.a65apps.pandaananass.tetsapplication.models

import android.net.Uri

data class FullContactModel(var id: String? = null,
                            var photo: Uri? = null,
                            var name: String? = null,
                            var firstNumber: String = "-",
                            var secondNumber: String = "-",
                            var firstMail: String = "-",
                            var secondMail: String = "-",
                            var description: String = "-",
                            var monthOfBirth: Int? = null,
                            var dayOfBirth: Int? = null)
