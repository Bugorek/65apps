package com.a65apps.pandaananass.tetsapplication.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.text.isDigitsOnly
import com.a65apps.pandaananass.tetsapplication.models.FullContactModel
import com.a65apps.pandaananass.tetsapplication.models.ShortContactModel
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
private const val SELECTION_CONTACT_SEARCH = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE?" +
        " OR " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE?"
private const val SQL_SYMBOL = "%"
private const val DATE_SEPARATOR = '-'
private const val MAXIMUM_DAYS_OF_MONTH = 31

object ContactDataSource {
    fun getContactDetails(context: Context, id: String): Single<FullContactModel> {
        return Single.fromCallable {
            Thread.sleep(1500)
            val contactDetails = FullContactModel()
            context.contentResolver?.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                SELECTION_CONTACTS,
                Array(1) { id },
                null)
                ?.use { contactCursor ->
                    if (contactCursor.moveToNext()) {
                        contactDetails.name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    }
                }
            contactDetails.id = id
            contactDetails.photo = getContactListPhoto(context = context, contactId = id)
            getContactNumber(context = context,contactDetails = contactDetails, id = id)
            getContactEmail(context = context,contactDetails = contactDetails, id = id)
            getContactNote(context = context,contactDetails = contactDetails, id = id)
            getContactBirthday(context = context,contactDetails = contactDetails, id = id)
            contactDetails
        }
    }

    private fun getContactNumber(context: Context,contactDetails: FullContactModel, id: String) {
        context.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                SELECTION_NUMBER,
                Array(1) { id },
                null)
                ?.use { numberCursor ->
                    if (numberCursor.moveToFirst()) {
                        contactDetails.firstNumber = numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    if (numberCursor.moveToNext()) {
                        numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))?.let {
                            contactDetails.secondNumber = it
                        }
                    }
                }
    }
    private fun getContactEmail(context: Context,contactDetails: FullContactModel, id: String) {
        context.contentResolver?.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                SELECTION_MAIL,
                Array(1) { id },
                null)
                ?.use { emailCursor ->
                    if (emailCursor.moveToFirst()) {
                        emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))?.let {
                            contactDetails.firstMail = it
                        }
                    }
                    if (emailCursor.moveToNext()) {
                        emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).let {
                            contactDetails.secondMail = it
                        }
                    }
                }
    }

    private fun getContactNote(context: Context,contactDetails: FullContactModel, id: String) {
        context.contentResolver?.query(ContactsContract.Data.CONTENT_URI,
                null,
                SELECTION_NOTE,
                Array(1) { id },
                null)
                ?.use { noteCursor ->
                    if (noteCursor.moveToNext()) {
                        noteCursor.getString(noteCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE))?.let {
                            contactDetails.description = it
                        }
                    }
                }
    }

    private fun getContactBirthday(context: Context,contactDetails: FullContactModel, id: String) {
        context.contentResolver?.query(ContactsContract.Data.CONTENT_URI,
                Array(1) { ContactsContract.CommonDataKinds.Event.DATA },
                SELECTION_BIRTHDAY,
                Array(1) { id },
                null)
                ?.use { birthdayCursor ->
                    if (birthdayCursor.moveToNext()) {
                        val dataList = this.dateConversion(birthdayCursor.getString(0).toString())
                        if (dataList.isNotEmpty() && dataList.size == 3) {
                            if (dataList[0] > MAXIMUM_DAYS_OF_MONTH) {
                                contactDetails.dayOfBirth = dataList[2]
                                contactDetails.monthOfBirth = dataList[1]
                            } else {
                                contactDetails.dayOfBirth = dataList[0]
                                contactDetails.monthOfBirth = dataList[1]
                            }
                        }
                    }
                }
    }

    fun getContactList(context: Context, query: String?): Single<List<ShortContactModel>> =
            Single.fromCallable {
                Thread.sleep(1500)
                if (query != null) {
                contactListWithQuery(context = context, query = query)
                } else {
                    contactListWithoutQuery(context = context)
                }
            }

    private fun dateConversion(dateString: String): List<Int> =
            try {
                dateString.split(DATE_SEPARATOR).filter { it.isDigitsOnly() }.map { it.toInt() }
            } catch (ex: Exception) {
                listOf()
            }

    private fun contactListWithQuery(context: Context, query: String): List<ShortContactModel> {
        val contactList = mutableListOf<ShortContactModel>()
        val newQuery = "$SQL_SYMBOL$query$SQL_SYMBOL"
        context.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                SELECTION_CONTACT_SEARCH,
                arrayOf(newQuery, newQuery),
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                ?.use { contactCursor ->
                    while (contactCursor.moveToNext()) {
                        val id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                        val name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        val contactNumber = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val photoUri = getContactListPhoto(context = context, contactId = id)
                        contactList.add(ShortContactModel(id = id,
                                photo = photoUri,
                                name = name,
                                number = contactNumber))
                    }
                }
        return contactList
    }

    private fun contactListWithoutQuery(context: Context): List<ShortContactModel> {
        val contactList = mutableListOf<ShortContactModel>()
        context.contentResolver?.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null)
                ?.use { contactCursor ->
                    while (contactCursor.moveToNext()) {
                        val id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        var contactNumber: String? = null
                        val photoUri = getContactListPhoto(context = context, contactId = id)
                        context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                SELECTION_NUMBER,
                                Array(1) { id },
                                null)
                                ?.use { numberCursor ->
                                    if (numberCursor.moveToNext()) {
                                        val number = numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                        contactNumber = number
                                    }
                                }
                        contactList.add(ShortContactModel(id = id,
                                photo = photoUri,
                                name = name,
                                number = contactNumber))
                    }
                }
        return contactList
    }

    private fun getContactListPhoto(context: Context, contactId: String): Uri? {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId.toLong())
        val photoPath = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        var photoUri: Uri? = null
        context.contentResolver.query(photoPath, arrayOf(ContactsContract.Contacts.Photo.PHOTO), null, null, null)
                ?.use { photoCursor ->
                    Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                    if (photoCursor.moveToFirst()) {
                        photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                    }
                }
        return photoUri
    }
}