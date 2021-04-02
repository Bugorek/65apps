package com.a65apps.pandaananass.tetsapplication.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity

const val PERMISSION_DIALOG_NAME = "PermissionDialog"

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mainActivity = activity as MainActivity
        return AlertDialog.Builder(requireActivity())
                .setMessage(R.string.permission_dialog_message)
                .setPositiveButton(R.string.permission_dialog_positive_button) { _, _ ->
                    mainActivity.positiveButtonClick()
                }
                .setNegativeButton(R.string.permission_dialog_negative_button) { _, _ ->
                    mainActivity.negativeButtonClick()
                }
                .create()
    }
}