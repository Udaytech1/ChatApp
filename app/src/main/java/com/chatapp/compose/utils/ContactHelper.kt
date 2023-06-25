package com.chatapp.compose.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.chatapp.compose.ui.home_screen.HomeViewModel

data class ContactData(
    val contactId: String,
    val name: String,
    val phoneNumber: String,
    val avatar: String?,
    val status: String = "Offline",
)

class ContactHelper(val context: Context) {
    val contentResolver: ContentResolver = context.contentResolver
    val cursor: Cursor? = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    @SuppressLint("Range")
    suspend fun getAllContact(homeViewModel: HomeViewModel) {
        val list = ArrayList<ContactData>()
        var name = ""
        var number = ""
        if (cursor != null) {
            for (i in 1 until cursor.count) {
                if (cursor.moveToNext()) {
                    name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    Log.d("Name", name)
                    val id =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val photoURI =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                    val phones: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null,
                        null
                    )
                    if (phones != null) {
                        while (phones.moveToNext()) {
                            if (!phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    .isNullOrEmpty()
                            ) {
                                number =
                                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            }
                        }
                        phones.close()
                    }
                    if (!number.isNullOrEmpty()) {
                        list.add(ContactData(id, name, number.filter { it.isDigit() }, photoURI))
                        homeViewModel.addContact(
                            ContactData(
                                id,
                                name,
                                number.filter { it.isDigit() },
                                photoURI
                            )
                        )
                    }
                }
            }
            cursor.close()
        }
        Log.d("ContactHelper", "getAllContact: $list,")
    }
}