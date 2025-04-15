package com.example.mogflix.data.local

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {

    @TypeConverter
    fun fromTimeStamp(value: String?): Date? {
        return value?.let {
            SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).parse(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let {
            SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).format(date)
        }
    }
}