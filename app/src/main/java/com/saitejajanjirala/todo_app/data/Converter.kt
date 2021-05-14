package com.saitejajanjirala.todo_app.data

import androidx.room.TypeConverter
import java.util.*

class Converter {
    @TypeConverter
    fun fromTimeStamp(value:Long?)=value?.let{ Date(it) }
    @TypeConverter
    fun fromDate(value: Date?)=value?.time
}