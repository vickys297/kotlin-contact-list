package com.example.contactlist.util

import com.example.contactlist.dataModel.ContactModel

interface AppInterface {

    interface ContactInterface{
        fun onContactSelected(item: ContactModel)
    }
}