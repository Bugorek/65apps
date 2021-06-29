package com.a65apps.pandaananass.tetsapplication.contactDetails

import android.Manifest
import android.content.Context
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
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.a65apps.pandaananass.tetsapplication.common.PermissionDialogClickListener
import com.a65apps.pandaananass.tetsapplication.main.MainActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.domain.contactDetails.FullContactModel
import com.github.rahatarmanahmed.cpv.CircularProgressView
import javax.inject.Inject
import javax.inject.Provider

private const val ARGUMENT_ID = "Id"
const val FRAGMENT_DETAILS_NAME = "ContactDetailsFragment"

class ContactDetailsFragment :
    MvpAppCompatFragment(),
    ContactDetailsView,
    PermissionDialogClickListener {
    @Inject
    lateinit var contactDetailsPresenterProvider: Provider<ContactDetailsPresenter>

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
    private var cpvWait: CircularProgressView? = null
    private var txtRequestError: TextView? = null
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val contactId = arguments?.getString(ARGUMENT_ID)
                contactId?.let { contactDetailsPresenter.getContactData(id = it) }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    contactDetailsPresenter.showPermissionDialog(activity = requireActivity())
                } else {
                    txtNoPermission?.visibility = View.VISIBLE
                }
            }
        }

    companion object {
        fun getNewInstance(id: String): ContactDetailsFragment =
            ContactDetailsFragment().apply {
                arguments = bundleOf(
                    ARGUMENT_ID to id
                )
            }
    }

    override fun onAttach(context: Context) {
        val appDelegate = requireActivity().application as? ComponentOwner
        val contactDataComponent = appDelegate?.getAppComponent()
            ?.contactDetailsFactory?.create()
        contactDataComponent?.inject(contactDetailsFragment = this)
        super.onAttach(context)
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
        val mainActivity = activity as? MainActivity
        val contactId = arguments?.getString(ARGUMENT_ID)
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
        cpvWait = view.findViewById(R.id.cpv_contact_details)
        txtRequestError = view.findViewById(R.id.txt_contact_list_request_error)
        txtDescription?.movementMethod = ScrollingMovementMethod()
        mainActivity?.title = resources.getString(R.string.fragment_contact_details_title)
        contactDetailsPresenter.getPermission(
            contactId = contactId,
            context = requireContext(),
            activity = requireActivity(),
            requestPermission = requestPermission
        )
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
        cpvWait = null
        txtRequestError = null
        super.onDestroyView()
    }

    override fun getContactData() {
        cpvWait?.visibility = View.VISIBLE
        arguments?.getString(ARGUMENT_ID)?.let {
            contactDetailsPresenter.getContactData(id = it)
        }
    }

    override fun setContactData(contactModel: FullContactModel?) {
        if (contactModel == null) {
            txtRequestError?.visibility = View.VISIBLE
        } else {
            cpvWait?.visibility = View.GONE
            txtName?.text = contactModel.name
            txtFirstNumber?.text = contactModel.firstNumber
            txtFirstMail?.text = contactModel.firstMail
            txtSecondNumber?.text = contactModel.secondNumber
            txtSecondMail?.text = contactModel.secondMail
            txtDescription?.text = contactModel.description
            imgContactAvatar?.setImageURI(contactModel.photo?.toUri())
            if (contactModel.dayOfBirth == null) {
                txtBirthday?.text = getString(R.string.fragment_contact_details_empty_birthday)
                btnNotification?.visibility = View.GONE
                rlContact?.visibility = View.VISIBLE
            } else {
                setBirthday(contactModel)
            }
        }
    }

    override fun notificationButtonStyle(notificationStatus: Boolean) {
        if (notificationStatus) {
            btnNotification?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
            btnNotification?.text = getString(R.string.fragment_contact_delete_btn_txt)
        } else {
            btnNotification?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
            btnNotification?.text = getString(R.string.fragment_contact_add_btn_txt)
        }
    }

    override fun buttonClickStatus(clickStatus: Boolean) {
        if (clickStatus) {
            requestPermission.launch(Manifest.permission.READ_CONTACTS)
        } else {
            txtNoPermission?.visibility = View.VISIBLE
        }
    }

    @ProvidePresenter
    fun providePresenter(): ContactDetailsPresenter {
        return contactDetailsPresenterProvider.get()
    }

    private fun setBirthday(contactModel: FullContactModel) {
        val contactId = arguments?.getString(ARGUMENT_ID)
            ?: throw IllegalArgumentException("contact id is required")
        btnNotification?.setOnClickListener {
            contactDetailsPresenter.notificationClick(contactModel)
        }
        contactDetailsPresenter.notificationButtonStyle(contactId, txtName?.text.toString())
        txtBirthday?.text = getString(
            R.string.fragment_contact_details_birthday,
            contactModel.dayOfBirth,
            contactModel.monthOfBirth
        )
        rlContact?.visibility = View.VISIBLE
    }
}
