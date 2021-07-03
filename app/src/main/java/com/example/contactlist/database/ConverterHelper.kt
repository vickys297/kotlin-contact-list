package com.example.contactlist.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConverterHelper {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(list: ArrayList<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String): ArrayList<String> {
        return gson.fromJson(json, object : TypeToken<ArrayList<String?>?>() {}.type)
    }

}