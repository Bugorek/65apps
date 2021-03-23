package com.a65apps.pandaananass.tetsapplication.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.a65apps.pandaananass.tetsapplication.R
import com.a65apps.pandaananass.tetsapplication.activity.MainActivity
import com.a65apps.pandaananass.tetsapplication.interfaces.ContactServiceInterface
import com.a65apps.pandaananass.tetsapplication.interfaces.ServiceOwner
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.a65apps.pandaananass.tetsapplication.receivers.AlarmReceiver
import java.lang.ref.WeakReference
import java.util.*

private const val ARGUMENT_ID = "Id"
private const val ARGUMENT_NAME = "Name"
private const val ACTION = "com.a65apps.pandaananass.tetsapplication.receivers"
const val FRAGMENT_DETAILS_NAME = "ContactDetailsFragment"

class ContactDetailsFragment : Fragment(), ContactServiceInterface {

    private var serviceOwner: ServiceOwner? = null
    private var rlContact: RelativeLayout? = null
    private var txtName: TextView? = null
    private var txtFirstNumber: TextView? = null
    private var txtFirstMail: TextView? = null
    private var txtSecondNumber: TextView? = null
    private var txtSecondMail: TextView? = null
    private var txtDescription: TextView? = null
    private var txtBirthday: TextView? = null
    private var btnNotification: Button? = null
    private var contactMonthOfBirth: Int? = null
    private var contactDayOfBirth: Int? = null

    companion object {
        fun getNewInstance(id: Int): ContactDetailsFragment {
            val contactDetailsFragment = ContactDetailsFragment()
            val args = Bundle()
            args.putInt(ARGUMENT_ID, id)
            contactDetailsFragment.arguments = args
            return contactDetailsFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ServiceOwner) {
            serviceOwner = context
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
        btnNotification = view.findViewById(R.id.btn_contact_notification)
        btnNotification?.setOnClickListener {
            if (notificationStatus(id = id)) {
                deleteNotification(intent = getIntent(), id = id)
            } else {
                setNotification(intent = getIntent(), id = id)
            }
        }
        if (notificationStatus(id = id)) {
            btnNotification?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            btnNotification?.text = getString(R.string.fragment_contact_delete_btn_txt)
        } else {
            btnNotification?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            btnNotification?.text = getString(R.string.fragment_contact_add_btn_txt)
        }
        txtDescription?.movementMethod = ScrollingMovementMethod()
        mainActivity.title = resources.getString(R.string.fragment_contact_details_title)
        if (savedInstanceState == null) {
            getContactData()
        }
    }

    override fun onDestroyView() {
        txtName = null
        txtFirstNumber = null
        txtFirstMail = null
        txtSecondNumber = null
        txtSecondMail = null
        txtDescription = null
        rlContact = null
        contactMonthOfBirth = null
        contactDayOfBirth = null
        super.onDestroyView()
    }

    override fun onDetach() {
        serviceOwner = null
        super.onDetach()
    }

    @SuppressLint("StringFormatMatches")
    fun setContactDetails(contactModel: FullContactModel) {
        activity?.runOnUiThread {
            contactMonthOfBirth = contactModel.monthOfBirth
            contactDayOfBirth = contactModel.dayOfBirth
            txtName?.text = contactModel.name
            txtFirstNumber?.text = contactModel.firstNumber
            txtFirstMail?.text = contactModel.firstMail
            txtSecondNumber?.text = contactModel.secondNumber
            txtSecondMail?.text = contactModel.secondMail
            txtDescription?.text = contactModel.description
            txtBirthday?.text = getString(R.string.fragment_contact_details_birthday, contactDayOfBirth, contactMonthOfBirth)
            rlContact?.visibility = View.VISIBLE
        }
    }

    override fun getContactData() {
        val weakFragment = WeakReference(this)
        arguments?.getInt(ARGUMENT_ID)?.let {
            serviceOwner?.getService()?.getFullContactData(weakFragment, it)
        }
    }

    private fun setNotification(intent: Intent, id: Int?) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = id?.let { it -> PendingIntent.getBroadcast(requireContext(), it, intent, 0) }
        if (contactMonthOfBirth != null && contactDayOfBirth != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, birthdayCalendar(dayOfBirth = contactDayOfBirth!!, monthOfBirth = contactMonthOfBirth!!).timeInMillis, pendingIntent)
        }
        btnNotification?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        btnNotification?.text = getString(R.string.fragment_contact_delete_btn_txt)
    }

    private fun deleteNotification(intent: Intent, id: Int?) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = id?.let { it -> PendingIntent.getBroadcast(requireContext(), it, intent, 0) }
        alarmManager.cancel(pendingIntent)
        pendingIntent?.cancel()
        btnNotification?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        btnNotification?.text = getString(R.string.fragment_contact_add_btn_txt)
    }

    private fun notificationStatus(id: Int?): Boolean {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val status = id?.let { PendingIntent.getBroadcast(requireContext(), it, intent, PendingIntent.FLAG_NO_CREATE) }
        return status != null
    }

    private fun birthdayCalendar(dayOfBirth: Int, monthOfBirth: Int): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        if (month - monthOfBirth + 1 < 0) {
            calendar.set(Calendar.YEAR, year)
        } else if (month - monthOfBirth + 1 == 0) {
            if (day - dayOfBirth > 0) {
                calendar.set(Calendar.YEAR, year + 1)
            } else {
                calendar.set(Calendar.YEAR, year)
            }
        } else {
            calendar.set(Calendar.YEAR, year + 1)
        }
        calendar.set(Calendar.MONTH, monthOfBirth - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfBirth)
        return calendar
    }

    private fun getIntent(): Intent {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val id = arguments?.getInt(ARGUMENT_ID)
        intent.putExtra(ARGUMENT_ID, id)
        intent.putExtra(ARGUMENT_NAME, txtName?.text)
        intent.action = ACTION
        return intent
    }
}