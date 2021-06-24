package com.a65apps.pandaananass.tetsapplication.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.contact_details.FRAGMENT_DETAILS_NAME
import com.a65apps.pandaananass.tetsapplication.contact_list.FRAGMENT_LIST_NAME

const val PERMISSION_DIALOG_NAME = "PermissionDialog"

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contactFragment = if (activity?.supportFragmentManager?.findFragmentByTag(
                FRAGMENT_DETAILS_NAME
            ) != null
        ) {
            activity?.supportFragmentManager?.findFragmentByTag(FRAGMENT_DETAILS_NAME) as? PermissionDialogClickListener
        } else {
            activity?.supportFragmentManager?.findFragmentByTag(FRAGMENT_LIST_NAME) as? PermissionDialogClickListener
        }
        return AlertDialog.Builder(requireActivity())
            .setMessage(R.string.permission_dialog_message)
            .setPositiveButton(R.string.permission_dialog_positive_button) { _, _ ->
                contactFragment?.positiveClick()
            }
            .setNegativeButton(R.string.permission_dialog_negative_button) { _, _ ->
                contactFragment?.negativeClick()
            }
            .create()
    }
}