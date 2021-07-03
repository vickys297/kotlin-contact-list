package com.example.contactlist.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.contactlist.dataModel.ContactModel

@Dao
interface ContactDAO {

    @Query("SELECT * FROM contactTable ORDER BY name ASC")
    fun getContactList(): PagingSource<Int, ContactModel>

    @Query("SELECT COUNT(*) from contactTable limit 1")
    fun isContactAvailable(): LiveData<Int>

    @Transaction
    suspend fun updateList(list: List<ContactModel>) {
        deleteAll()
        insertAll(list)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(arrayContactModel: List<ContactModel>)

    @Query("DELETE FROM contactTable")
    fun deleteAll()

}