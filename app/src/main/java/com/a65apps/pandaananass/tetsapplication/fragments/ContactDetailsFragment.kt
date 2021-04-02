package com.a65apps.pandaananass.tetsapplication.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.interfaces.PermissionDialogClickListener
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.a65apps.pandaananass.tetsapplication.presenters.ContactDetailsPresenter
import com.a65apps.pandaananass.tetsapplication.views.ContactDetailsView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

private const val ARGUMENT_ID = "Id"
const val FRAGMENT_DETAILS_NAME = "ContactDetailsFragment"

class ContactDetailsFragment : MvpAppCompatFragment(), ContactDetailsView, PermissionDialogClickListener {

    @InjectPresenter
    lateinit var contactDetailsPresenter: ContactDetailsPresenter

    private var rlContact: RelativeLayout? = null
    private var txtName: TextView? = null
    private var txtFirstNumber: TextView? = null
    private var txtFirstMail: TextView? = null
    private var txtSecondNumber: TextView? = null
    private var txtSecondMail: TextView? = null
    private var txtDescription: TextView? = null
    private var txtBirthday: TextView? = null
    private var btnNotification: Button? = null
    private var imgContactAvatar: ImageView? = null
    private var txtNoPermission: TextView? = null
    private var contactMonthOfBirth: Int? = null
    private var contactDayOfBirth: Int? = null
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val contactId = arguments?.getString(ARGUMENT_ID)
            contactId?.let { contactDetailsPresenter.getContactData(context = requireContext(), id = it) }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {
                contactDetailsPresenter.showPermissionDialog(activity = requireActivity())
            } else {
                contactDetailsPresenter.setNoPermission()
            }
        }
    }

    companion object {
        fun getNewInstance(id: String): ContactDetailsFragment {
            val contactDetailsFragment = ContactDetailsFragment()
            val args = Bundle()
            args.putString(ARGUMENT_ID, id)
            contactDetailsFragment.arguments = args
            return contactDetailsFragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contactdetails, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        rlContact = view.findViewById(R.id.rl_contact_details)
        txtName = view.findViewById(R.id.txt_contact_name)
        txtFirstNumber = view.findViewById(R.id.txt_contact_first_number)
        txtFirstMail = view.findViewById(R.id.txt_contact_first_mail)
        txtSecondNumber = view.findViewById(R.id.txt_contact_second_number)
        txtSecondMail = view.findViewById(R.id.txt_contact_second_mail)
        txtDescription = view.findViewById(R.id.txt_contact_description)
        txtBirthday = view.findViewById(R.id.txt_contact_birthday)
        imgContactAvatar = view.findViewById(R.id.img_contact_avatar)
        btnNotification = view.findViewById(R.id.btn_contact_notification)
        txtNoPermission = view.findViewById(R.id.txt_contact_details_no_permission)
        txtDescription?.movementMethod = ScrollingMovementMethod()
        mainActivity.title = resources.getString(R.string.fragment_contact_details_title)
        getPermission()
    }

    override fun onDestroyView() {
        txtName = null
        txtFirstNumber = null
        txtFirstMail = null
        txtSecondNumber = null
        txtSecondMail = null
        txtDescription = null
        rlContact = null
        imgContactAvatar = null
        txtNoPermission = null
        contactMonthOfBirth = null
        contactDayOfBirth = null
        super.onDestroyView()
    }

    override fun getContactData() {
        arguments?.getString(ARGUMENT_ID)?.let {
            contactDetailsPresenter.getContactData(requireContext(), id = it)
        }
    }

    override fun setContactData(contactModel: FullContactModel) {
        activity?.runOnUiThread {
            txtName?.text = contactModel.name
            txtFirstNumber?.text = contactModel.firstNumber
            txtFirstMail?.text = contactModel.firstMail
            txtSecondNumber?.text = contactModel.secondNumber
            txtSecondMail?.text = contactModel.secondMail
            txtDescription?.text = contactModel.description
            imgContactAvatar?.setImageURI(contactModel.photo)
        }
    }

    override fun noBirthday() {
        txtBirthday?.text = getString(R.string.fragment_contact_details_empty_birthday)
        btnNotification?.visibility = View.GONE
        rlContact?.visibility = View.VISIBLE
    }

    override fun setBirthday(contactModel: FullContactModel) {
        activity?.runOnUiThread {
            val contactId = arguments?.getString(ARGUMENT_ID) ?: throw IllegalArgumentException("contact id is required")
            btnNotification?.setOnClickListener {
                contactDetailsPresenter.notificationClick(context = requireContext(),
                        contactId = contactId,
                        contactName = txtName?.text.toString(),
                        monthOfBirth = contactModel.monthOfBirth,
                        dayOfBirth = contactModel.dayOfBirth
                )
            }
            contactDetailsPresenter.notificationButtonStyle(context = requireContext(),
                    contactId = contactId,
                    contactName = txtName?.text.toString())
            txtBirthday?.text = getString(R.string.fragment_contact_details_birthday, contactModel.dayOfBirth, contactModel.monthOfBirth)
            rlContact?.visibility = View.VISIBLE
        }
    }

    override fun notificationSet() {
        btnNotification?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        btnNotification?.text = getString(R.string.fragment_contact_delete_btn_txt)
    }

    override fun notificationNotSet() {
        btnNotification?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        btnNotification?.text = getString(R.string.fragment_contact_add_btn_txt)
    }

    override fun setNoPermission() {
        txtNoPermission?.visibility = View.VISIBLE
    }

    override fun negativeClick() {
        contactDetailsPresenter.setNoPermission()
    }

    override fun positiveClick() {
        requestPermission.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    val contactId = arguments?.getString(ARGUMENT_ID)
                    contactId?.let { contactDetailsPresenter.getContactData(context = requireContext(), id = it) }
                }
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS) -> {
                    contactDetailsPresenter.showPermissionDialog(activity = requireActivity())
                }
                else -> {
                    requestPermission.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }
    }
}