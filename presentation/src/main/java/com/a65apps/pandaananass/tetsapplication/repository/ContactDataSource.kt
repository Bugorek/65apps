package com.a65apps.pandaananass.tetsapplication.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.text.isDigitsOnly
import com.example.domain.contactDetails.ContactDetailsOwner
import com.example.domain.contactDetails.FullContactModel
import com.example.domain.contactList.ContactListOwner
import com.example.domain.contactList.ShortContactModel
import io.reactivex.rxjava3.core.Single

private const val SELECTION_CONTACTS = ContactsContract.Contacts._ID + " =?"
private const val SELECTION_NUMBER = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
private const val SELECTION_MAIL = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =?"
private const val SELECTION_NOTE = ContactsContract.Data.RAW_CONTACT_ID + "=?" +
    " AND " + ContactsContract.Data.MIMETYPE + "='" +
    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE + "'"
private const val SELECTION_BIRTHDAY = ContactsContract.Data.CONTACT_ID + "=?" +
    " AND " + ContactsContract.Data.MIMETYPE + " = '" +
    ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE +
    "' AND " + ContactsContract.CommonDataKinds.Event.TYPE +
    " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
private const val SELECTION_CONTACT_SEARCH =
    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE?" +
        " OR " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE?"
private const val SQL_SYMBOL = "%"
private const val DATE_SEPARATOR = '-'
private const val MAXIMUM_DAYS_OF_MONTH = 31
private const val TIME_OF_THREAD_SLEEP: Long = 1500
private const val CORRECT_NUMBER_OF_DATE_ELEMENT = 3

class ContactDataSource(private val context: Context) : ContactListOwner, ContactDetailsOwner {
    override fun getContactDetails(id: String): Single<FullContactModel> {
        return Single.fromCallable {
            Thread.sleep(TIME_OF_THREAD_SLEEP)
            val contactDetails = FullContactModel()
            context.contentResolver?.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                SELECTION_CONTACTS,
                Array(1) { id },
                null
            )
                ?.use { contactCursor ->
                    if (contactCursor.moveToNext()) {
                        contactDetails.name =
                            contactCursor.getString(
                                contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            )
                    }
                }
            contactDetails.id = id
            contactDetails.photo = getContactListPhoto(context, id)
            val numberList = getContactNumber(context, id)
            if (numberList.isNotEmpty() && numberList.size == 1) {
                contactDetails.firstNumber = numberList[0]
            } else if (numberList.isNotEmpty() && numberList.size == 2) {
                contactDetails.firstNumber = numberList[0]
                contactDetails.secondNumber = numberList[1]
            }
            getContactEmail(context = context, contactDetails = contactDetails, id = id)
            getContactNote(context = context, contactDetails = contactDetails, id = id)
            getContactBirthday(context = context, contactDetails = contactDetails, id = id)
            contactDetails
        }
    }

    override fun getContactList(query: String?): Single<List<ShortContactModel>> =
        Single.fromCallable {
            Thread.sleep(TIME_OF_THREAD_SLEEP)
            if (query != null) {
                contactListWithQuery(context, query)
            } else {
                contactListWithoutQuery(context)
            }
        }

    private fun getContactNumber(context: Context, id: String): List<String> {
        val numberList: ArrayList<String> = arrayListOf()
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            SELECTION_NUMBER,
            Array(1) { id },
            null
        )
            ?.use { numberCursor ->
                if (numberCursor.moveToFirst()) {
                    numberList.add(
                        numberCursor.getString(
                            numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                    )
                }
                if (numberCursor.moveToNext()) {
                    numberCursor.getString(
                        numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                        ?.let {
                            numberList.add(it)
                        }
                }
            }
        return numberList
    }

    private fun getContactEmail(context: Context, contactDetails: FullContactModel, id: String) {
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            SELECTION_MAIL,
            Array(1) { id },
            null
        )
            ?.use { emailCursor ->
                if (emailCursor.moveToFirst()) {
                    emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ?.let {
                            contactDetails.firstMail = it
                        }
                }
                if (emailCursor.moveToNext()) {
                    emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        .let {
                            contactDetails.secondMail = it
                        }
                }
            }
    }

    private fun getContactNote(context: Context, contactDetails: FullContactModel, id: String) {
        context.contentResolver?.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            SELECTION_NOTE,
            Array(1) { id },
            null
        )
            ?.use { noteCursor ->
                if (noteCursor.moveToNext()) {
                    noteCursor.getString(noteCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE))
                        ?.let {
                            contactDetails.description = it
                        }
                }
            }
    }

    private fun getContactBirthday(context: Context, contactDetails: FullContactModel, id: String) {
        var dataList: List<Int> = listOf()
        context.contentResolver?.query(
            ContactsContract.Data.CONTENT_URI,
            Array(1) { ContactsContract.CommonDataKinds.Event.DATA },
            SELECTION_BIRTHDAY,
            Array(1) { id },
            null
        )
            ?.use { birthdayCursor ->
                if (birthdayCursor.moveToNext()) {
                    dataList = this.dateConversion(birthdayCursor.getString(0).toString())
                }
            }
        if (dataList.isNotEmpty() && dataList.size == CORRECT_NUMBER_OF_DATE_ELEMENT) {
            if (dataList[0] > MAXIMUM_DAYS_OF_MONTH) {
                contactDetails.dayOfBirth = dataList[2]
                contactDetails.monthOfBirth = dataList[1]
            } else {
                contactDetails.dayOfBirth = dataList[0]
                contactDetails.monthOfBirth = dataList[1]
            }
        }
    }

    private fun dateConversion(dateString: String): List<Int> =
        try {
            dateString.split(DATE_SEPARATOR).filter { it.isDigitsOnly() }.map { it.toInt() }
        } catch (ex: NumberFormatException) {
            throw NumberFormatException(ex.message)
            listOf()
        }

    private fun contactListWithQuery(context: Context, query: String): List<ShortContactModel> {
        val contactList = mutableListOf<ShortContactModel>()
        val newQuery = "$SQL_SYMBOL$query$SQL_SYMBOL"
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            SELECTION_CONTACT_SEARCH,
            arrayOf(newQuery, newQuery),
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )
            ?.use { contactCursor ->
                while (contactCursor.moveToNext()) {
                    val id =
                        contactCursor.getString(
                            contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                        )
                    val name =
                        contactCursor.getString(
                            contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        )
                    val contactNumber =
                        contactCursor.getString(
                            contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                    val photoUri = getContactListPhoto(context = context, contactId = id)
                    contactList.add(
                        ShortContactModel(
                            id = id,
                            photo = photoUri,
                            name = name,
                            number = contactNumber
                        )
                    )
                }
            }
        return contactList
    }

    private fun contactListWithoutQuery(context: Context): List<ShortContactModel> {
        val contactList = mutableListOf<ShortContactModel>()
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
            ?.use { contactCursor ->
                while (contactCursor.moveToNext()) {
                    val id =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    var contactNumber: String? = null
                    if (getContactNumber(context = context, id = id).isNotEmpty()) {
                        contactNumber = getContactNumber(context = context, id = id)[0]
                    }
                    val photoUri = getContactListPhoto(context = context, contactId = id)
                    contactList.add(
                        ShortContactModel(
                            id = id,
                            photo = photoUri,
                            name = name,
                            number = contactNumber
                        )
                    )
                }
            }
        return contactList
    }

    private fun getContactListPhoto(context: Context, contactId: String): String? {
        val contactUri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId.toLong())
        val photoPath =
            Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        var photoUri: Uri? = null
        context.contentResolver.query(
            photoPath,
            arrayOf(ContactsContract.Contacts.Photo.PHOTO),
            null,
            null,
            null
        )
            ?.use { photoCursor ->
                Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                if (photoCursor.moveToFirst()) {
                    photoUri = Uri.withAppendedPath(
                        contactUri,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                    )
                }
            }
        return photoUri.toString()
    }
}
