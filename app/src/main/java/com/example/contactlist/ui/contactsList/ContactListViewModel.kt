package com.example.contactlist.ui.contactsList

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.database.AppDatabase
import com.example.contactlist.util.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.paging.PagingConfig as PagingConfig1

internal val TAG_CONTACT_VIEW_MODEL = ContactListViewModel::class.java.canonicalName

class ContactListViewModel(
    private val repository: Repository,
    val dataBundle: Bundle?
) : ViewModel() {


    private var contactDataLive: Flow<PagingData<ContactModel>>

    init {
        Log.i(TAG_CONTACT_VIEW_MODEL, "ViewModel init: ")
        contactDataLive = Pager(PagingConfig1(pageSize = 25, enablePlaceholders = false)) {
            repository.contactDatabase.getContactList()
        }.flow
            .cachedIn(viewModelScope)
    }

    fun getContactCount(): LiveData<Int> {
        return repository.contactDatabase.isContactAvailable()
    }

    fun getContacts(): Flow<PagingData<ContactModel>> {
        Log.i(TAG_CONTACT_VIEW_MODEL, "getContacts: ")
        return contactDataLive
    }

    suspend fun syncLocalDatabase(list: List<ContactModel>) {
        repository.contactDatabase.updateList(list)
    }


}