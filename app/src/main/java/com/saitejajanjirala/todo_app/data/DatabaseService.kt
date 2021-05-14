package com.saitejajanjirala.todo_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Task::class],version = 1,exportSchema = false)
@TypeConverters(Converter::class)
abstract class DatabaseService:RoomDatabase() {
    abstract fun getTaskDao():TaskDao
}