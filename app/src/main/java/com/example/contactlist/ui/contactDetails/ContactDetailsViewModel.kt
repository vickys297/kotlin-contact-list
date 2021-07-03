package com.example.contactlist.ui.contactDetails

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.util.AppConstance
import com.example.contactlist.util.Repository

class ContactDetailsViewModel:
    ViewModel() {

    var contactData  = MutableLiveData<ContactModel>()

}