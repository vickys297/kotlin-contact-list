package com.example.contactlist.dataModel

import android.os.Parcelable
import androidx.room.*
import com.example.contactlist.util.AppConstance
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName = AppConstance.DB.CONTACT_TABLE,
    indices = [Index(value = ["id"], unique = true)]
)
data class ContactModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "pictureUri")
    val pictureUri: String? = "",

    @ColumnInfo(name = "thumbnailUri")
    val thumbnailUri: String? = "",

    @ColumnInfo(name = "phone", defaultValue = "")
    var phone: ArrayList<String> = ArrayList(),

    @ColumnInfo(name = "email", defaultValue = "")
    var email: ArrayList<String> = ArrayList()
) : Parcelable {


}
