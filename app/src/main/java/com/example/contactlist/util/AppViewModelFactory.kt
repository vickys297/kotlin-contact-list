package com.example.contactlist.util

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactlist.ui.contactsList.ContactListViewModel
import java.lang.Exception

class AppViewModelFactory(
    private val repository: Repository,
    private val dataBundle: Bundle?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass.canonicalName) {
            ContactListViewModel::class.java.canonicalName -> {
                ContactListViewModel(
                    repository,
                    dataBundle
                ) as T
            }
            else -> {
                throw Exception("No View Model Found")
            }
        }
    }
}


