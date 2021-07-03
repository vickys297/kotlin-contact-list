package com.example.contactlist.util

import android.R.array
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.util.LongSparseArray
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.util.Repository
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


internal val TAG = ContactHelperClass::class.java.canonicalName

class ContactHelperClass(val context: Context, repository: Repository) {

    companion object {
        fun getInstance(context: Context, repository: Repository) =
            ContactHelperClass(context, repository)
    }


    fun getContactList(): ArrayList<ContactModel> {

        val arrayList = ArrayList<ContactModel>()
        val arrayLongSparse = LongSparseArray<ContactModel>()

        val contentResolver = context.contentResolver
        val start = System.currentTimeMillis()

        val projection = arrayOf(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.CommonDataKinds.Contactables.DATA,
            ContactsContract.CommonDataKinds.Contactables.TYPE
        )
        val selection = ContactsContract.Data.MIMETYPE + " in (?, ?)"
        val selectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        val sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY

        val uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)


        cursor?.run {

            val mimeTypeIdx = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE)
            val idIdx = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
            val nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val dataIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA)
            val typeIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE)

            val thumbnailIdx = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
            val photoIdx = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIdx)
                val type = cursor.getInt(typeIdx)
                val name = cursor.getString(nameIdx)
                val data = cursor.getString(dataIdx)
                val mimeType = cursor.getString(mimeTypeIdx)

                val thumbnailUri = cursor.getString(thumbnailIdx)
                val photoUri = cursor.getString(photoIdx)

                var contactModel: ContactModel? = arrayLongSparse.get(id)
                if (contactModel == null) {
                    contactModel = ContactModel(
                        id = id, name = name, pictureUri = photoUri, thumbnailUri = thumbnailUri
                    )
                    arrayLongSparse.put(id, contactModel)
                    arrayList.add(contactModel)
                }


                if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                    if (!contactModel.email.contains(data))
                        contactModel.email.add(data)
                } else {
                    if (!contactModel.phone.contains(data))
                        contactModel.phone.add(data)
                }
            }


            val ms = System.currentTimeMillis() - start


            arrayList.forEach {
                Log.i(TAG, "getContactList: ${it.name}/${it.phone}/${it.email}")
            }
            Log.i(TAG, "getContactList: $ms")
            cursor.close()
        }

        return arrayList
    }

}