package com.example.contactlist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.util.AppConstance
import java.lang.reflect.Type



@Database(
    entities = [ContactModel::class],
    version = 1
)
@TypeConverters(ConverterHelper::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDAO


    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: dataBaseConfig(context).also { instance = it }
            }


        private fun dataBaseConfig(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppConstance.DB.DATABASE_NAME
            ).build()
        }

    }
}