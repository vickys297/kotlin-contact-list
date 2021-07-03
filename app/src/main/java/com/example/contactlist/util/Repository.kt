package com.example.contactlist.util

import android.content.Context
import androidx.annotation.WorkerThread
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.database.AppDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class Repository(val context: Context, private val appDatabase: AppDatabase) {


    val contactDatabase = appDatabase.contactDao()

    companion object {
        fun getInstance(context: Context, appDatabase: AppDatabase): Repository {
            return Repository(context, appDatabase)
        }
    }





}